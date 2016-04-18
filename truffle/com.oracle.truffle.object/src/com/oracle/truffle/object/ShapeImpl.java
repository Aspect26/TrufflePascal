/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.DynamicObjectFactory;
import com.oracle.truffle.api.object.Layout;
import com.oracle.truffle.api.object.Location;
import com.oracle.truffle.api.object.LocationFactory;
import com.oracle.truffle.api.object.ObjectLocation;
import com.oracle.truffle.api.object.ObjectType;
import com.oracle.truffle.api.object.Property;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.api.object.ShapeListener;
import com.oracle.truffle.api.utilities.NeverValidAssumption;
import com.oracle.truffle.object.LocationImpl.InternalLongLocation;
import com.oracle.truffle.object.LocationImpl.LocationVisitor;
import com.oracle.truffle.object.Locations.ConstantLocation;
import com.oracle.truffle.object.Locations.DeclaredDualLocation;
import com.oracle.truffle.object.Locations.DeclaredLocation;
import com.oracle.truffle.object.Locations.DualLocation;
import com.oracle.truffle.object.Locations.ValueLocation;
import com.oracle.truffle.object.Transition.AddPropertyTransition;
import com.oracle.truffle.object.Transition.DirectReplacePropertyTransition;
import com.oracle.truffle.object.Transition.ObjectTypeTransition;
import com.oracle.truffle.object.Transition.PropertyTransition;
import com.oracle.truffle.object.Transition.RemovePropertyTransition;
import com.oracle.truffle.object.Transition.ReservePrimitiveArrayTransition;

/**
 * Shape objects create a mapping of Property objects to indexes. The mapping of those indexes to an
 * actual store is not part of Shape's role, but JSObject's. Shapes are immutable; adding or
 * deleting a property yields a new Shape which links to the old one. This allows inline caching to
 * simply check the identity of an object's Shape to determine if the cache is valid. There is one
 * exception to this immutability, the transition map, but that is used simply to assure that an
 * identical series of property additions and deletions will yield the same Shape object.
 *
 * @see DynamicObject
 * @see Property
 * @see Locations
 */
public abstract class ShapeImpl extends Shape {
    private final int id;

    protected final LayoutImpl layout;
    protected final ObjectType objectType;
    protected final ShapeImpl parent;
    protected final PropertyMap propertyMap;

    private final Object extraData;
    private final Object sharedData;
    private final ShapeImpl root;

    protected final int objectArraySize;
    protected final int objectArrayCapacity;
    protected final int objectFieldSize;
    protected final int primitiveFieldSize;
    protected final int primitiveArraySize;
    protected final int primitiveArrayCapacity;
    protected final boolean hasPrimitiveArray;

    protected final int depth;
    protected final int propertyCount;

    protected final Assumption validAssumption;
    @CompilationFinal protected volatile Assumption leafAssumption;

    public static final String NO_FASTPATH_PROPERTY_ADD_MESSAGE = "don't add object properties in compiled code";

    /**
     * Shape transition map; lazily initialized.
     *
     * @see #getTransitionMapForRead()
     * @see #getTransitionMapForWrite()
     */
    private volatile Map<Transition, ShapeImpl> transitionMap;

    private final Transition transitionFromParent;

    /**
     * Private constructor.
     *
     * @param parent predecessor shape
     * @param transitionFromParent direct transition from parent shape
     *
     * @see #ShapeImpl(Layout, ShapeImpl, ObjectType, Object, PropertyMap, Transition,
     *      BaseAllocator, int)
     */
    private ShapeImpl(Layout layout, ShapeImpl parent, ObjectType objectType, Object sharedData, PropertyMap propertyMap, Transition transitionFromParent, int objectArraySize, int objectFieldSize,
                    int primitiveFieldSize, int primitiveArraySize, boolean hasPrimitiveArray, int id) {
        this.layout = (LayoutImpl) layout;
        this.objectType = Objects.requireNonNull(objectType);
        this.propertyMap = Objects.requireNonNull(propertyMap);
        this.root = parent != null ? parent.getRoot() : this;
        this.parent = parent;

        this.objectArraySize = objectArraySize;
        this.objectArrayCapacity = capacityFromSize(objectArraySize);
        this.objectFieldSize = objectFieldSize;
        this.primitiveFieldSize = primitiveFieldSize;
        this.primitiveArraySize = primitiveArraySize;
        this.primitiveArrayCapacity = capacityFromSize(primitiveArraySize);
        this.hasPrimitiveArray = hasPrimitiveArray;

        if (parent != null) {
            this.propertyCount = makePropertyCount(parent, propertyMap);
            this.depth = parent.depth + 1;
        } else {
            this.propertyCount = 0;
            this.depth = 0;
        }

        this.validAssumption = createValidAssumption();

        this.id = id;
        this.transitionFromParent = transitionFromParent;
        this.sharedData = sharedData;
        this.extraData = objectType.createShapeData(this);

        shapeCount.inc();
        if (ObjectStorageOptions.DumpShapes) {
            Debug.trackShape(this);
        }
    }

    protected ShapeImpl(Layout layout, ShapeImpl parent, ObjectType operations, Object sharedData, PropertyMap propertyMap, Transition transition, Allocator allocator, int id) {
        this(layout, parent, operations, sharedData, propertyMap, transition, ((BaseAllocator) allocator).objectArraySize, ((BaseAllocator) allocator).objectFieldSize,
                        ((BaseAllocator) allocator).primitiveFieldSize, ((BaseAllocator) allocator).primitiveArraySize, ((BaseAllocator) allocator).hasPrimitiveArray, id);
    }

    @SuppressWarnings("hiding")
    protected abstract ShapeImpl createShape(Layout layout, Object sharedData, ShapeImpl parent, ObjectType operations, PropertyMap propertyMap, Transition transition, Allocator allocator, int id);

    protected ShapeImpl(Layout layout, ObjectType operations, Object sharedData, int id) {
        this(layout, null, operations, sharedData, PropertyMap.empty(), null, layout.createAllocator(), id);
    }

    private static int makePropertyCount(ShapeImpl parent, PropertyMap propertyMap) {
        if (propertyMap.size() > parent.propertyMap.size()) {
            Property lastProperty = propertyMap.getLastProperty();
            if (!lastProperty.isHidden() && !lastProperty.isShadow()) {
                return parent.propertyCount + 1;
            }
        }
        return parent.propertyCount;
    }

    @Override
    public final Property getLastProperty() {
        return propertyMap.getLastProperty();
    }

    @Override
    public final int getId() {
        return this.id;
    }

    /**
     * Calculate array size for the given number of elements.
     */
    private static int capacityFromSize(int size) {
        if (size == 0) {
            return 0;
        } else if (size < 4) {
            return 4;
        } else if (size < 32) {
            return ((size + 7) / 8) * 8;
        } else {
            return ((size + 15) / 16) * 16;
        }
    }

    public final int getObjectArraySize() {
        return objectArraySize;
    }

    public final int getObjectFieldSize() {
        return objectFieldSize;
    }

    public final int getPrimitiveFieldSize() {
        return primitiveFieldSize;
    }

    public final int getObjectArrayCapacity() {
        return objectArrayCapacity;
    }

    public final int getPrimitiveArrayCapacity() {
        return primitiveArrayCapacity;
    }

    public final int getPrimitiveArraySize() {
        return primitiveArraySize;
    }

    public final boolean hasPrimitiveArray() {
        return hasPrimitiveArray;
    }

    /**
     * Get the (parent) shape that holds the given property.
     */
    public final ShapeImpl getShapeFromProperty(Object propertyName) {
        ShapeImpl current = this;
        while (current != getRoot()) {
            if (current.getTransitionFromParent() instanceof AddPropertyTransition && ((AddPropertyTransition) current.getTransitionFromParent()).getProperty().getKey().equals(propertyName)) {
                return current;
            }
            current = current.getParent();
        }

        return null;
    }

    /**
     * Get the (parent) shape that holds the given property.
     */
    public final ShapeImpl getShapeFromProperty(Property prop) {
        ShapeImpl current = this;
        while (current != getRoot()) {
            if (current.getTransitionFromParent() instanceof AddPropertyTransition && ((AddPropertyTransition) current.getTransitionFromParent()).getProperty().equals(prop)) {
                return current;
            }
            current = current.parent;
        }

        return null;
    }

    /**
     * Get a property entry by string name.
     *
     * @param key the name to look up
     * @return a Property object, or null if not found
     */
    @Override
    @TruffleBoundary
    public Property getProperty(Object key) {
        return propertyMap.get(key);
    }

    protected final void addDirectTransition(Transition transition, ShapeImpl next) {
        assert next.getParent() == this && transition.isDirect();
        addTransitionInternal(transition, next);
    }

    public final void addIndirectTransition(Transition transition, ShapeImpl next) {
        assert next.getParent() != this && !transition.isDirect();
        addTransitionInternal(transition, next);
    }

    private void addTransitionInternal(Transition transition, ShapeImpl next) {
        getTransitionMapForWrite().put(transition, next);
    }

    public final Map<Transition, ShapeImpl> getTransitionMapForRead() {
        return transitionMap != null ? transitionMap : Collections.<Transition, ShapeImpl> emptyMap();
    }

    private Map<Transition, ShapeImpl> getTransitionMapForWrite() {
        if (transitionMap != null) {
            return transitionMap;
        } else {
            synchronized (getMutex()) {
                if (transitionMap != null) {
                    return transitionMap;
                }
                invalidateLeafAssumption();
                return transitionMap = new ConcurrentHashMap<>();
            }
        }
    }

    public final PropertyMap getPropertyMap() {
        return propertyMap;
    }

    protected final ShapeImpl queryTransition(Transition transition) {
        return queryTransition(transition, true);
    }

    protected final ShapeImpl queryTransition(Transition transition, boolean ensureValid) {
        ShapeImpl cachedShape = this.getTransitionMapForRead().get(transition);
        if (cachedShape != null) { // Shape already exists?
            shapeCacheHitCount.inc();
            return ensureValid ? layout.getStrategy().ensureValid(cachedShape) : cachedShape;
        }
        shapeCacheMissCount.inc();

        return null;
    }

    /**
     * Add a new property in the map, yielding a new or cached Shape object.
     *
     * @param property the property to add
     * @return the new Shape
     */
    @TruffleBoundary
    @Override
    public ShapeImpl addProperty(Property property) {
        assert isValid();
        onPropertyTransition(property);
        return addPropertyInternal(property, true);
    }

    private void onPropertyTransition(Property property) {
        if (sharedData instanceof ShapeListener) {
            ((ShapeListener) sharedData).onPropertyTransition(property.getKey());
        }
    }

    @TruffleBoundary
    @Override
    public ShapeImpl defineProperty(Object key, Object value, int flags) {
        return defineProperty(key, value, flags, DEFAULT_LAYOUT_FACTORY);
    }

    @TruffleBoundary
    @Override
    public ShapeImpl defineProperty(Object key, Object value, int flags, LocationFactory locationFactory) {
        ShapeImpl oldShape = this;
        if (!oldShape.isValid()) {
            oldShape = layout.getStrategy().ensureValid(oldShape);
        }
        PropertyImpl existing = (PropertyImpl) oldShape.getProperty(key);
        if (existing == null) {
            return oldShape.addProperty(Property.create(key, locationFactory.createLocation(oldShape, value), flags));
        } else {
            if (existing.getFlags() == flags) {
                if (existing.getLocation().canSet(value)) {
                    return oldShape;
                } else {
                    if (existing.getLocation() instanceof DeclaredLocation) {
                        return oldShape.addProperty(existing.relocateShadow(locationFactory.createLocation(oldShape, value)));
                    } else {
                        return (ShapeImpl) layout.getStrategy().generalizeProperty(existing, value, oldShape, oldShape).getShape();
                    }
                }
            } else {
                Property newProperty = Property.create(key, oldShape.getLayout().existingLocationForValue(value, existing.getLocation(), oldShape), flags);
                return oldShape.replaceProperty(existing, newProperty);
            }
        }
    }

    /**
     * Add a new property in the map, yielding a new or cached Shape object.
     *
     * In contrast to {@link ShapeImpl#addProperty(Property)}, this method does not care about
     * obsolete shapes.
     *
     * @see #addProperty(Property)
     */
    private ShapeImpl addPropertyInternal(Property prop, boolean ensureValid) {
        CompilerAsserts.neverPartOfCompilation(NO_FASTPATH_PROPERTY_ADD_MESSAGE);
        assert prop.isShadow() || !(this.hasProperty(prop.getKey())) : "duplicate property " + prop.getKey();

        AddPropertyTransition addTransition = new AddPropertyTransition(prop);
        ShapeImpl cachedShape = queryTransition(addTransition, ensureValid);
        if (cachedShape != null) {
            return cachedShape;
        }

        ShapeImpl oldShape = layout.getStrategy().ensureSpace(this, prop.getLocation());

        ShapeImpl newShape = makeShapeWithAddedProperty(oldShape, addTransition);
        oldShape.addDirectTransition(addTransition, newShape);
        return newShape;
    }

    protected ShapeImpl cloneRoot(ShapeImpl from, Object newSharedData) {
        return createShape(from.layout, newSharedData, null, from.objectType, from.propertyMap, null, from.allocator(), from.id);
    }

    /**
     * Create a separate clone of a shape.
     *
     * @param newParent the cloned parent shape
     */
    protected final ShapeImpl cloneOnto(ShapeImpl newParent) {
        ShapeImpl from = this;
        ShapeImpl newShape = createShape(newParent.layout, newParent.sharedData, newParent, from.objectType, from.propertyMap, from.transitionFromParent, from.allocator(), newParent.id);

        shapeCloneCount.inc();

        // (aw) need to have this transition for obsolescence
        newParent.addDirectTransition(from.transitionFromParent, newShape);
        return newShape;
    }

    public final Transition getTransitionFromParent() {
        return transitionFromParent;
    }

    /**
     * Create a new shape that adds a property to the parent shape.
     */
    private static ShapeImpl makeShapeWithAddedProperty(ShapeImpl parent, AddPropertyTransition addTransition) {
        Property addend = addTransition.getProperty();
        BaseAllocator allocator = parent.allocator().addLocation(addend.getLocation());

        PropertyMap newPropertyMap = parent.propertyMap.putCopy(addend);

        ShapeImpl newShape = parent.createShape(parent.layout, parent.sharedData, parent, parent.objectType, newPropertyMap, addTransition, allocator, parent.id);
        assert ((LocationImpl) addend.getLocation()).primitiveArrayCount() == 0 || newShape.hasPrimitiveArray;
        assert newShape.depth == allocator.depth;
        return newShape;
    }

    /**
     * Create a new shape that reserves the primitive extension array field.
     */
    private static ShapeImpl makeShapeWithPrimitiveExtensionArray(ShapeImpl parent, Transition transition) {
        assert parent.getLayout().hasPrimitiveExtensionArray();
        assert !parent.hasPrimitiveArray();
        BaseAllocator allocator = parent.allocator().addLocation(parent.getLayout().getPrimitiveArrayLocation());
        ShapeImpl newShape = parent.createShape(parent.layout, parent.sharedData, parent, parent.objectType, parent.propertyMap, transition, allocator, parent.id);
        assert newShape.hasPrimitiveArray();
        assert newShape.depth == allocator.depth;
        return newShape;
    }

    private ShapeImpl addPrimitiveExtensionArray() {
        assert layout.hasPrimitiveExtensionArray() && !hasPrimitiveArray();
        Transition transition = new ReservePrimitiveArrayTransition();
        ShapeImpl cachedShape = queryTransition(transition);
        if (cachedShape != null) {
            return cachedShape;
        }

        ShapeImpl oldShape = layout.getStrategy().ensureSpace(this, layout.getPrimitiveArrayLocation());
        ShapeImpl newShape = makeShapeWithPrimitiveExtensionArray(oldShape, transition);
        oldShape.addDirectTransition(transition, newShape);
        return newShape;
    }

    /**
     * Are these two shapes related, i.e. do they have the same root?
     *
     * @param other Shape to compare to
     * @return true if one shape is an upcast of the other, or the Shapes are equal
     */
    @Override
    public boolean isRelated(Shape other) {
        if (this == other) {
            return true;
        }
        if (this.getRoot() == getRoot()) {
            return true;
        }
        return false;
    }

    /**
     * Get a list of all properties that this Shape stores.
     *
     * @return list of properties
     */
    @TruffleBoundary
    @Override
    public final List<Property> getPropertyList(Pred<Property> filter) {
        LinkedList<Property> props = new LinkedList<>();
        next: for (Iterator<Property> it = this.propertyMap.reverseOrderedValueIterator(); it.hasNext();) {
            Property currentProperty = it.next();

            if (!currentProperty.isHidden() && filter.test(currentProperty)) {
                if (currentProperty.getLocation() instanceof DeclaredLocation) {
                    for (Iterator<Property> iter = props.iterator(); iter.hasNext();) {
                        Property other = iter.next();
                        if (other.isShadow() && other.getKey().equals(currentProperty.getKey())) {
                            iter.remove();
                            props.addFirst(other);
                            continue next;
                        }
                    }
                }
                props.addFirst(currentProperty);
            }
        }
        return props;
    }

    @Override
    public final List<Property> getPropertyList() {
        return getPropertyList(ALL);
    }

    /**
     * Returns all (also hidden) Property objects in this shape.
     *
     * @param ascending desired order
     */
    @TruffleBoundary
    @Override
    public final List<Property> getPropertyListInternal(boolean ascending) {
        LinkedList<Property> props = new LinkedList<>();
        for (Iterator<Property> it = this.propertyMap.reverseOrderedValueIterator(); it.hasNext();) {
            Property current = it.next();
            if (ascending) {
                props.addFirst(current);
            } else {
                props.add(current);
            }
        }
        return props;
    }

    /**
     * Get a list of all (visible) property names in insertion order.
     *
     * @return list of property names
     */
    @TruffleBoundary
    @Override
    public final List<Object> getKeyList(Pred<Property> filter) {
        LinkedList<Object> keys = new LinkedList<>();
        for (Iterator<Property> it = this.propertyMap.reverseOrderedValueIterator(); it.hasNext();) {
            Property currentProperty = it.next();
            if (!currentProperty.isHidden() && filter.test(currentProperty) && !currentProperty.isShadow()) {
                keys.addFirst(currentProperty.getKey());
            }
        }
        return keys;
    }

    @Override
    public final List<Object> getKeyList() {
        return getKeyList(ALL);
    }

    @Override
    public Iterable<Object> getKeys() {
        return getKeyList();
    }

    @Override
    public final boolean isValid() {
        return getValidAssumption().isValid();
    }

    @Override
    public final Assumption getValidAssumption() {
        return validAssumption;
    }

    private static Assumption createValidAssumption() {
        return Truffle.getRuntime().createAssumption("valid shape");
    }

    public final void invalidateValidAssumption() {
        getValidAssumption().invalidate();
    }

    @Override
    public final boolean isLeaf() {
        return leafAssumption == null || leafAssumption.isValid();
    }

    @Override
    public final Assumption getLeafAssumption() {
        if (leafAssumption == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            synchronized (getMutex()) {
                if (leafAssumption == null) {
                    leafAssumption = isLeafHelper() ? createLeafAssumption() : NeverValidAssumption.INSTANCE;
                }
            }
        }
        return leafAssumption;
    }

    private boolean isLeafHelper() {
        return getTransitionMapForRead().isEmpty();
    }

    private static Assumption createLeafAssumption() {
        return Truffle.getRuntime().createAssumption("leaf shape");
    }

    private void invalidateLeafAssumption() {
        Assumption assumption = leafAssumption;
        if (assumption != null) {
            assumption.invalidate();
        } else {
            leafAssumption = NeverValidAssumption.INSTANCE;
        }
    }

    @Override
    public String toString() {
        return toStringLimit(Integer.MAX_VALUE);
    }

    @TruffleBoundary
    public String toStringLimit(int limit) {
        StringBuilder sb = new StringBuilder();
        sb.append('@');
        sb.append(Integer.toHexString(hashCode()));
        if (!isValid()) {
            sb.append('!');
        }

        sb.append("{");
        for (Iterator<Property> iterator = propertyMap.reverseOrderedValueIterator(); iterator.hasNext();) {
            Property p = iterator.next();
            sb.append(p);
            if (iterator.hasNext()) {
                sb.append(", ");
            }
            if (sb.length() >= limit) {
                sb.append("...");
                break;
            }
            sb.append("\n");
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public final ShapeImpl getParent() {
        return parent;
    }

    public final int getDepth() {
        return depth;
    }

    @Override
    public final boolean hasProperty(Object name) {
        return getProperty(name) != null;
    }

    @TruffleBoundary
    @Override
    public final ShapeImpl removeProperty(Property prop) {
        onPropertyTransition(prop);

        RemovePropertyTransition transition = new RemovePropertyTransition(prop);
        ShapeImpl cachedShape = queryTransition(transition);
        if (cachedShape != null) {
            return cachedShape;
        }

        ShapeImpl shape = getShapeFromProperty(prop.getKey());
        if (shape != null) {
            List<Transition> transitionList = new ArrayList<>();
            ShapeImpl current = this;
            while (current != shape) {
                if (!(current.getTransitionFromParent() instanceof Transition.DirectReplacePropertyTransition) ||
                                !((Transition.DirectReplacePropertyTransition) current.getTransitionFromParent()).getPropertyBefore().getKey().equals(prop.getKey())) {
                    transitionList.add(current.getTransitionFromParent());
                }
                current = current.parent;
            }
            ShapeImpl newShape = shape.parent;
            for (ListIterator<Transition> iterator = transitionList.listIterator(transitionList.size()); iterator.hasPrevious();) {
                Transition previous = iterator.previous();
                newShape = newShape.applyTransition(previous, true);
            }

            addIndirectTransition(transition, newShape);
            return newShape;
        } else {
            return null;
        }
    }

    @TruffleBoundary
    @Override
    public final ShapeImpl append(Property oldProperty) {
        return addProperty(oldProperty.relocate(allocator().moveLocation(oldProperty.getLocation())));
    }

    public final ShapeImpl applyTransition(Transition transition, boolean append) {
        if (transition instanceof AddPropertyTransition) {
            return append ? append(((AddPropertyTransition) transition).getProperty()) : addPropertyInternal(((AddPropertyTransition) transition).getProperty(), false);
        } else if (transition instanceof ObjectTypeTransition) {
            return changeType(((ObjectTypeTransition) transition).getObjectType());
        } else if (transition instanceof ReservePrimitiveArrayTransition) {
            return reservePrimitiveExtensionArray();
        } else if (transition instanceof DirectReplacePropertyTransition) {
            Property oldProperty = ((DirectReplacePropertyTransition) transition).getPropertyBefore();
            Property newProperty = ((DirectReplacePropertyTransition) transition).getPropertyAfter();
            if (append) {
                assert oldProperty.getLocation() instanceof DualLocation && newProperty.getLocation() instanceof DualLocation;
                oldProperty = getProperty(oldProperty.getKey());
                newProperty = newProperty.relocate(((DualLocation) oldProperty.getLocation()).changeType(((DualLocation) newProperty.getLocation()).getType()));
            }
            return replaceProperty(oldProperty, newProperty);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public final BaseAllocator allocator() {
        return layout.getStrategy().createAllocator(this);
    }

    /**
     * Duplicate shape exchanging existing property with new property.
     */
    @Override
    public ShapeImpl replaceProperty(Property oldProperty, Property newProperty) {
        return directReplaceProperty(oldProperty, newProperty);
    }

    protected final ShapeImpl directReplaceProperty(Property oldProperty, Property newProperty) {
        assert oldProperty.getKey().equals(newProperty.getKey());
        onPropertyTransition(oldProperty);

        Transition replacePropertyTransition = new Transition.DirectReplacePropertyTransition(oldProperty, newProperty);
        ShapeImpl cachedShape = queryTransition(replacePropertyTransition);
        if (cachedShape != null) {
            return cachedShape;
        }
        PropertyMap newPropertyMap = this.getPropertyMap().replaceCopy(oldProperty, newProperty);
        ShapeImpl newShape = createShape(getLayout(), getSharedData(), this, getObjectType(), newPropertyMap, replacePropertyTransition, allocator(), getId());

        addDirectTransition(replacePropertyTransition, newShape);
        return newShape;
    }

    /**
     * Find lowest common ancestor of two related shapes.
     */
    public static ShapeImpl findCommonAncestor(ShapeImpl left, ShapeImpl right) {
        if (!left.isRelated(right)) {
            throw new IllegalArgumentException("shapes must have the same root");
        } else if (left == right) {
            return left;
        }
        int leftLength = left.depth;
        int rightLength = right.depth;
        ShapeImpl leftPtr = left;
        ShapeImpl rightPtr = right;
        while (leftLength > rightLength) {
            leftPtr = leftPtr.parent;
            leftLength--;
        }
        while (rightLength > leftLength) {
            rightPtr = rightPtr.parent;
            rightLength--;
        }
        while (leftPtr != rightPtr) {
            leftPtr = leftPtr.parent;
            rightPtr = rightPtr.parent;
        }
        return leftPtr;
    }

    @Override
    public final int getPropertyCount() {
        return propertyCount;
    }

    /**
     * Find difference between two shapes.
     *
     * @see ObjectStorageOptions#TraceReshape
     */
    public static List<Property> diff(Shape oldShape, Shape newShape) {
        List<Property> oldList = oldShape.getPropertyListInternal(false);
        List<Property> newList = newShape.getPropertyListInternal(false);

        List<Property> diff = new ArrayList<>(oldList);
        diff.addAll(newList);
        List<Property> intersection = new ArrayList<>(oldList);
        intersection.retainAll(newList);
        diff.removeAll(intersection);
        return diff;
    }

    @Override
    public ObjectType getObjectType() {
        return objectType;
    }

    @Override
    public ShapeImpl getRoot() {
        return root;
    }

    @Override
    public final boolean check(DynamicObject subject) {
        return subject.getShape() == this;
    }

    @Override
    public final LayoutImpl getLayout() {
        return layout;
    }

    @Override
    public final Object getData() {
        return extraData;
    }

    @Override
    public final Object getSharedData() {
        return sharedData;
    }

    @TruffleBoundary
    @Override
    public final boolean hasTransitionWithKey(Object key) {
        for (Transition transition : getTransitionMapForRead().keySet()) {
            if (transition instanceof PropertyTransition) {
                if (((PropertyTransition) transition).getProperty().getKey().equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Clone off a separate shape with new shared data.
     */
    @TruffleBoundary
    @Override
    public final ShapeImpl createSeparateShape(Object newSharedData) {
        if (parent == null) {
            return cloneRoot(this, newSharedData);
        } else {
            return this.cloneOnto(parent.createSeparateShape(newSharedData));
        }
    }

    @Override
    @TruffleBoundary
    public final ShapeImpl changeType(ObjectType newOps) {
        ObjectTypeTransition transition = new ObjectTypeTransition(newOps);
        ShapeImpl cachedShape = queryTransition(transition);
        if (cachedShape != null) {
            return cachedShape;
        }

        ShapeImpl newShape = createShape(layout, sharedData, this, newOps, propertyMap, transition, allocator(), id);
        addDirectTransition(transition, newShape);
        return newShape;
    }

    @Override
    public final ShapeImpl reservePrimitiveExtensionArray() {
        if (layout.hasPrimitiveExtensionArray() && !hasPrimitiveArray()) {
            return addPrimitiveExtensionArray();
        }
        return this;
    }

    @Override
    public final Iterable<Property> getProperties() {
        return getPropertyList();
    }

    @Override
    public final DynamicObject newInstance() {
        return layout.newInstance(this);
    }

    @Override
    public final DynamicObjectFactory createFactory() {
        final List<Property> properties = getPropertyListInternal(true);
        for (Iterator<Property> iterator = properties.iterator(); iterator.hasNext();) {
            Property property = iterator.next();
            // skip non-instance fields
            assert property.getLocation() != layout.getPrimitiveArrayLocation();
            if (property.getLocation() instanceof ValueLocation) {
                iterator.remove();
            }
        }

        return new DynamicObjectFactory() {
            @CompilationFinal private final PropertyImpl[] instanceFields = properties.toArray(new PropertyImpl[properties.size()]);

            @ExplodeLoop
            public DynamicObject newInstance(Object... initialValues) {
                DynamicObject store = ShapeImpl.this.newInstance();
                CompilerAsserts.partialEvaluationConstant(instanceFields.length);
                for (int i = 0; i < instanceFields.length; i++) {
                    instanceFields[i].setInternal(store, initialValues[i]);
                }
                return store;
            }

            public Shape getShape() {
                return ShapeImpl.this;
            }
        };
    }

    @Override
    public Object getMutex() {
        return getRoot();
    }

    @Override
    public Shape tryMerge(Shape other) {
        return null;
    }

    public <R> R accept(ShapeVisitor<R> visitor) {
        return visitor.visitShape(this);
    }

    public abstract static class BaseAllocator extends Allocator implements LocationVisitor, Cloneable {
        protected final LayoutImpl layout;
        protected int objectArraySize;
        protected int objectFieldSize;
        protected int primitiveFieldSize;
        protected int primitiveArraySize;
        protected boolean hasPrimitiveArray;
        protected int depth;

        protected BaseAllocator(LayoutImpl layout) {
            this.layout = layout;
        }

        protected BaseAllocator(ShapeImpl shape) {
            this(shape.getLayout());
            this.objectArraySize = shape.objectArraySize;
            this.objectFieldSize = shape.objectFieldSize;
            this.primitiveFieldSize = shape.primitiveFieldSize;
            this.primitiveArraySize = shape.primitiveArraySize;
            this.hasPrimitiveArray = shape.hasPrimitiveArray;
            this.depth = shape.depth;
        }

        protected abstract Location moveLocation(Location oldLocation);

        protected abstract Location newObjectLocation(boolean useFinal, boolean nonNull);

        protected abstract Location newTypedObjectLocation(boolean useFinal, Class<?> type, boolean nonNull);

        protected abstract Location newIntLocation(boolean useFinal);

        protected abstract Location newDoubleLocation(boolean useFinal);

        protected abstract Location newLongLocation(boolean useFinal);

        protected abstract Location newBooleanLocation(boolean useFinal);

        @Override
        public final Location constantLocation(Object value) {
            return new ConstantLocation(value);
        }

        @Override
        protected Location locationForValue(Object value, boolean useFinal, boolean nonNull) {
            if (value instanceof Integer) {
                return newIntLocation(useFinal);
            } else if (value instanceof Double) {
                return newDoubleLocation(useFinal);
            } else if (value instanceof Long) {
                return newLongLocation(useFinal);
            } else if (value instanceof Boolean) {
                return newBooleanLocation(useFinal);
            } else if (ObjectStorageOptions.TypedObjectLocations && value != null) {
                return newTypedObjectLocation(useFinal, value.getClass(), nonNull);
            }
            return newObjectLocation(useFinal, nonNull && value != null);
        }

        protected abstract Location locationForValueUpcast(Object value, Location oldLocation);

        @Override
        protected Location locationForType(Class<?> type, boolean useFinal, boolean nonNull) {
            if (type == int.class) {
                return newIntLocation(useFinal);
            } else if (type == double.class) {
                return newDoubleLocation(useFinal);
            } else if (type == long.class) {
                return newLongLocation(useFinal);
            } else if (type == boolean.class) {
                return newBooleanLocation(useFinal);
            } else if (ObjectStorageOptions.TypedObjectLocations && type != null && type != Object.class) {
                assert !type.isPrimitive() : "unsupported primitive type";
                return newTypedObjectLocation(useFinal, type, nonNull);
            }
            return newObjectLocation(useFinal, nonNull);
        }

        protected Location newDualLocation(Class<?> type) {
            return new DualLocation((InternalLongLocation) newLongLocation(false), (ObjectLocation) newObjectLocation(false, false), layout, type);
        }

        protected DualLocation newDualLocationForValue(Object value) {
            Class<?> initialType = null;
            if (value instanceof Integer) {
                initialType = int.class;
            } else if (value instanceof Double) {
                initialType = double.class;
            } else if (value instanceof Long) {
                initialType = long.class;
            } else if (value instanceof Boolean) {
                initialType = boolean.class;
            } else {
                initialType = Object.class;
            }
            return new DualLocation((InternalLongLocation) newLongLocation(false), (ObjectLocation) newObjectLocation(false, false), layout, initialType);
        }

        protected Location newDeclaredDualLocation(Object value) {
            return new DeclaredDualLocation((InternalLongLocation) newLongLocation(false), (ObjectLocation) newObjectLocation(false, false), value, layout);
        }

        protected <T extends Location> T advance(T location0) {
            if (location0 instanceof LocationImpl) {
                LocationImpl location = (LocationImpl) location0;
                if (location != layout.getPrimitiveArrayLocation()) {
                    location.accept(this);
                }
                if (layout.hasPrimitiveExtensionArray()) {
                    hasPrimitiveArray |= location == layout.getPrimitiveArrayLocation() || primitiveArraySize > 0;
                } else {
                    assert !hasPrimitiveArray && primitiveArraySize == 0;
                }
            }
            depth++;
            return location0;
        }

        @Override
        public BaseAllocator addLocation(Location location) {
            advance(location);
            return this;
        }

        public void visitObjectField(int index, int count) {
            objectFieldSize = Math.max(objectFieldSize, index + count);
        }

        public void visitObjectArray(int index, int count) {
            objectArraySize = Math.max(objectArraySize, index + count);
        }

        public void visitPrimitiveArray(int index, int count) {
            primitiveArraySize = Math.max(primitiveArraySize, index + count);
        }

        public void visitPrimitiveField(int index, int count) {
            primitiveFieldSize = Math.max(primitiveFieldSize, index + count);
        }

        @Override
        public final BaseAllocator copy() {
            return clone();
        }

        @Override
        protected final BaseAllocator clone() {
            try {
                return (BaseAllocator) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }

    /**
     * Match all filter.
     */
    private static final Pred<Property> ALL = new Pred<Property>() {
        public boolean test(Property t) {
            return true;
        }
    };

    static final LocationFactory DEFAULT_LAYOUT_FACTORY = new LocationFactory() {
        public Location createLocation(Shape shape, Object value) {
            return ((ShapeImpl) shape).allocator().locationForValue(value, true, value != null);
        }
    };

    private static final DebugCounter shapeCount = DebugCounter.create("Shapes allocated total");
    private static final DebugCounter shapeCloneCount = DebugCounter.create("Shapes allocated cloned");
    private static final DebugCounter shapeCacheHitCount = DebugCounter.create("Shape cache hits");
    private static final DebugCounter shapeCacheMissCount = DebugCounter.create("Shape cache misses");

    public ForeignAccess getForeignAccessFactory(DynamicObject object) {
        return getObjectType().getForeignAccessFactory(object);
    }
}
