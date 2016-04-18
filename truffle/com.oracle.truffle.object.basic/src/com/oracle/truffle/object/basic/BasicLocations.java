/*
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.object.basic;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.object.BooleanLocation;
import com.oracle.truffle.api.object.DoubleLocation;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.FinalLocationException;
import com.oracle.truffle.api.object.IncompatibleLocationException;
import com.oracle.truffle.api.object.IntLocation;
import com.oracle.truffle.api.object.Location;
import com.oracle.truffle.api.object.LongLocation;
import com.oracle.truffle.api.object.ObjectLocation;
import com.oracle.truffle.api.object.Property;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.object.LocationImpl;
import com.oracle.truffle.object.LocationImpl.InternalLongLocation;
import java.lang.invoke.MethodHandle;

/**
 * Property location.
 *
 * @see Shape
 * @see Property
 * @see DynamicObject
 */
public abstract class BasicLocations {
    static final int LONG_SIZE = 1;
    static final int OBJECT_SIZE = 1;

    public abstract static class ArrayLocation extends LocationImpl {
        protected final int index;
        protected final Location arrayLocation;

        public ArrayLocation(int index, Location arrayLocation) {
            this.index = index;
            this.arrayLocation = arrayLocation;
        }

        protected final Object getArray(DynamicObject store, boolean condition) {
            // non-null cast
            return arrayLocation.get(store, condition);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + index;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (!super.equals(obj)) {
                return false;
            }
            ArrayLocation other = (ArrayLocation) obj;
            if (index != other.index) {
                return false;
            }
            return true;
        }

        public final int getIndex() {
            return index;
        }

        @Override
        protected String getWhereString() {
            return "[" + index + "]";
        }
    }

    public abstract static class FieldLocation extends LocationImpl {
        private final int index;

        public FieldLocation(int index) {
            this.index = index;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + index;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (!super.equals(obj)) {
                return false;
            }
            FieldLocation other = (FieldLocation) obj;
            if (index != other.index) {
                return false;
            }
            return true;
        }

        public final int getIndex() {
            return index;
        }

        @Override
        protected String getWhereString() {
            return "@" + index;
        }
    }

    public abstract static class MethodHandleFieldLocation extends FieldLocation {
        protected final MethodHandle getter;
        protected final MethodHandle setter;

        public MethodHandleFieldLocation(int index, MethodHandle getter, MethodHandle setter) {
            super(index);
            this.getter = getter;
            this.setter = setter;
        }
    }

    public static class ObjectArrayLocation extends ArrayLocation implements ObjectLocation {
        public ObjectArrayLocation(int index, Location arrayLocation) {
            super(index, arrayLocation);
        }

        @Override
        public Object get(DynamicObject store, boolean condition) {
            return ((Object[]) getArray(store, condition))[index];
        }

        @Override
        public final void setInternal(DynamicObject store, Object value) throws IncompatibleLocationException {
            ((Object[]) getArray(store, false))[index] = value;
        }

        @Override
        public boolean canStore(Object value) {
            return true;
        }

        public Class<? extends Object> getType() {
            return Object.class;
        }

        public final boolean isNonNull() {
            return false;
        }

        @Override
        public int objectArrayCount() {
            return OBJECT_SIZE;
        }

        @Override
        public final void accept(LocationVisitor locationVisitor) {
            locationVisitor.visitObjectArray(index, OBJECT_SIZE);
        }
    }

    public static class ObjectFieldLocation extends MethodHandleFieldLocation implements ObjectLocation {

        public ObjectFieldLocation(int index, MethodHandle getter, MethodHandle setter) {
            super(index, getter, setter);
        }

        @Override
        public Object get(DynamicObject store, boolean condition) {
            try {
                return getter.invokeExact(store);
            } catch (Throwable e) {
                CompilerDirectives.transferToInterpreter();
                throw new IllegalStateException(e);
            }
        }

        @Override
        public final void setInternal(DynamicObject store, Object value) throws IncompatibleLocationException {
            try {
                setter.invokeExact(store, value);
            } catch (Throwable e) {
                CompilerDirectives.transferToInterpreter();
                throw new IllegalStateException(e);
            }
        }

        @Override
        public boolean canStore(Object value) {
            return true;
        }

        public Class<? extends Object> getType() {
            return Object.class;
        }

        public boolean isNonNull() {
            return false;
        }

        @Override
        public int objectFieldCount() {
            return OBJECT_SIZE;
        }

        @Override
        public final void accept(LocationVisitor locationVisitor) {
            locationVisitor.visitObjectField(getIndex(), OBJECT_SIZE);
        }
    }

    public abstract static class SimpleObjectFieldLocation extends FieldLocation implements ObjectLocation {

        public SimpleObjectFieldLocation(int index) {
            super(index);
        }

        @Override
        public abstract Object get(DynamicObject store, boolean condition);

        @Override
        public abstract void setInternal(DynamicObject store, Object value);

        @Override
        public boolean canStore(Object value) {
            return true;
        }

        public Class<? extends Object> getType() {
            return Object.class;
        }

        public boolean isNonNull() {
            return false;
        }

        @Override
        public int objectFieldCount() {
            return OBJECT_SIZE;
        }

        @Override
        public final void accept(LocationVisitor locationVisitor) {
            locationVisitor.visitObjectField(getIndex(), OBJECT_SIZE);
        }
    }

    public static class LongArrayLocation extends ArrayLocation implements InternalLongLocation {
        protected final boolean allowInt;

        public LongArrayLocation(int index, Location arrayLocation, boolean allowInt) {
            super(index, arrayLocation);
            this.allowInt = allowInt;
        }

        public LongArrayLocation(int index, Location arrayLocation) {
            this(index, arrayLocation, false);
        }

        @Override
        public final Object get(DynamicObject store, boolean condition) {
            return getLong(store, condition);
        }

        @Override
        public final void setInternal(DynamicObject store, Object value) throws IncompatibleLocationException {
            if (canStore(value)) {
                setLongInternal(store, ((Number) value).longValue());
            } else {
                throw incompatibleLocation();
            }
        }

        @Override
        public long getLong(DynamicObject store, boolean condition) {
            return ((long[]) getArray(store, condition))[index];
        }

        public final void setLongInternal(DynamicObject store, long value) {
            ((long[]) getArray(store, false))[index] = value;
        }

        @Override
        public void setLong(DynamicObject store, long value, Shape shape) throws FinalLocationException {
            setLongInternal(store, value);
        }

        @Override
        public final void setLong(DynamicObject store, long value, Shape oldShape, Shape newShape) {
            store.setShapeAndGrow(oldShape, newShape);
            setLongInternal(store, value);
        }

        @Override
        public final void setLong(DynamicObject store, long value) throws FinalLocationException {
            setLong(store, value, null);
        }

        public final long getLong(DynamicObject store, Shape shape) {
            return getLong(store, checkShape(store, shape));
        }

        @Override
        public final boolean canStore(Object value) {
            return value instanceof Long || (allowInt && value instanceof Integer);
        }

        public final Class<Long> getType() {
            return long.class;
        }

        @Override
        public int primitiveArrayCount() {
            return LONG_SIZE;
        }

        @Override
        public final void accept(LocationVisitor locationVisitor) {
            locationVisitor.visitPrimitiveArray(getIndex(), LONG_SIZE);
        }
    }

    public static class LongFieldLocation extends MethodHandleFieldLocation implements InternalLongLocation {
        public LongFieldLocation(int index, MethodHandle getter, MethodHandle setter) {
            super(index, getter, setter);
        }

        public static LongLocation create(InternalLongLocation longLocation, boolean allowInt) {
            if ((!allowInt && (longLocation instanceof LongLocationDecorator)) || (longLocation instanceof LongLocationDecorator && ((LongLocationDecorator) longLocation).allowInt == allowInt)) {
                return longLocation;
            } else {
                return new LongLocationDecorator(longLocation, allowInt);
            }
        }

        @Override
        public final Object get(DynamicObject store, boolean condition) {
            return getLong(store, condition);
        }

        @Override
        public final void setInternal(DynamicObject store, Object value) throws IncompatibleLocationException {
            if (canStore(value)) {
                setLongInternal(store, (long) value);
            } else {
                throw incompatibleLocation();
            }
        }

        @Override
        public final boolean canStore(Object value) {
            return value instanceof Long;
        }

        @Override
        public final void setLong(DynamicObject store, long value, Shape oldShape, Shape newShape) {
            store.setShapeAndGrow(oldShape, newShape);
            setLongInternal(store, value);
        }

        public long getLong(DynamicObject store, boolean condition) {
            try {
                return (long) getter.invokeExact(store);
            } catch (Throwable e) {
                CompilerDirectives.transferToInterpreter();
                throw new IllegalStateException(e);
            }
        }

        public void setLong(DynamicObject store, long value, Shape shape) {
            setLongInternal(store, value);
        }

        public final void setLong(DynamicObject store, long value) throws FinalLocationException {
            setLong(store, value, null);
        }

        public final void setLongInternal(DynamicObject store, long value) {
            try {
                setter.invokeExact(store, value);
            } catch (Throwable e) {
                CompilerDirectives.transferToInterpreter();
                throw new IllegalStateException(e);
            }
        }

        public final long getLong(DynamicObject store, Shape shape) {
            return getLong(store, checkShape(store, shape));
        }

        @Override
        public final int primitiveFieldCount() {
            return LONG_SIZE;
        }

        public final Class<Long> getType() {
            return long.class;
        }

        @Override
        public final void accept(LocationVisitor locationVisitor) {
            locationVisitor.visitPrimitiveField(getIndex(), LONG_SIZE);
        }
    }

    public static class LongLocationDecorator extends PrimitiveLocationDecorator implements InternalLongLocation {
        protected final boolean allowInt;

        public LongLocationDecorator(InternalLongLocation longLocation, boolean allowInt) {
            super(longLocation);
            this.allowInt = allowInt;
        }

        @Override
        public final Object get(DynamicObject store, boolean condition) {
            return getLong(store, condition);
        }

        @Override
        public final void setInternal(DynamicObject store, Object value) throws IncompatibleLocationException {
            if (canStore(value)) {
                setLongInternal(store, ((Number) value).longValue());
            } else {
                throw incompatibleLocation();
            }
        }

        @Override
        public final boolean canStore(Object value) {
            return value instanceof Long || (allowInt && value instanceof Integer);
        }

        @Override
        public final void setLong(DynamicObject store, long value, Shape oldShape, Shape newShape) {
            store.setShapeAndGrow(oldShape, newShape);
            setLongInternal(store, value);
        }

        public Class<Long> getType() {
            return long.class;
        }
    }

    public abstract static class SimpleLongFieldLocation extends FieldLocation implements InternalLongLocation {

        public SimpleLongFieldLocation(int index) {
            super(index);
        }

        @Override
        public final Object get(DynamicObject store, boolean condition) {
            return getLong(store, condition);
        }

        @Override
        public final void setInternal(DynamicObject store, Object value) throws IncompatibleLocationException {
            if (canStore(value)) {
                setLongInternal(store, ((Number) value).longValue());
            } else {
                throw incompatibleLocation();
            }
        }

        @Override
        public final boolean canStore(Object value) {
            return value instanceof Long;
        }

        @Override
        public final void setLong(DynamicObject store, long value, Shape oldShape, Shape newShape) {
            store.setShapeAndGrow(oldShape, newShape);
            setLongInternal(store, value);
        }

        public abstract long getLong(DynamicObject store, boolean condition);

        public final long getLong(DynamicObject store, Shape shape) {
            return getLong(store, checkShape(store, shape));
        }

        public final void setLong(DynamicObject store, long value) {
            setLong(store, value, null);
        }

        public void setLong(DynamicObject store, long value, Shape shape) {
            setLongInternal(store, value);
        }

        public abstract void setLongInternal(DynamicObject store, long value);

        @Override
        public final int primitiveFieldCount() {
            return LONG_SIZE;
        }

        public final Class<Long> getType() {
            return long.class;
        }

        @Override
        public final void accept(LocationVisitor locationVisitor) {
            locationVisitor.visitPrimitiveField(getIndex(), LONG_SIZE);
        }
    }

    public abstract static class PrimitiveLocationDecorator extends LocationImpl {
        private final InternalLongLocation longLocation;

        public PrimitiveLocationDecorator(InternalLongLocation longLocation) {
            this.longLocation = longLocation;
        }

        public final long getLong(DynamicObject store, Shape shape) {
            return longLocation.getLong(store, shape);
        }

        public final long getLong(DynamicObject store, boolean condition) {
            return longLocation.getLong(store, condition);
        }

        public final void setLong(DynamicObject store, long value, Shape shape) throws FinalLocationException {
            longLocation.setLong(store, value, shape);
        }

        public final void setLong(DynamicObject store, long value) throws FinalLocationException {
            longLocation.setLong(store, value);
        }

        public final void setLongInternal(DynamicObject store, long value) {
            longLocation.setLongInternal(store, value);
        }

        @Override
        public final int primitiveFieldCount() {
            return ((LocationImpl) longLocation).primitiveFieldCount();
        }

        @Override
        public final int primitiveArrayCount() {
            return ((LocationImpl) longLocation).primitiveArrayCount();
        }

        @Override
        public final void accept(LocationVisitor locationVisitor) {
            ((LocationImpl) longLocation).accept(locationVisitor);
        }
    }

    public static class IntLocationDecorator extends PrimitiveLocationDecorator implements IntLocation {
        public IntLocationDecorator(InternalLongLocation longLocation) {
            super(longLocation);
        }

        @Override
        public final Object get(DynamicObject store, boolean condition) {
            return getInt(store, condition);
        }

        public int getInt(DynamicObject store, boolean condition) {
            return (int) getLong(store, condition);
        }

        public void setInt(DynamicObject store, int value, Shape shape) throws FinalLocationException {
            setLong(store, value, shape);
        }

        @Override
        public final void setInt(DynamicObject store, int value) throws FinalLocationException {
            setInt(store, value, null);
        }

        @Override
        public final void setInternal(DynamicObject store, Object value) throws IncompatibleLocationException {
            if (canStore(value)) {
                setLongInternal(store, (int) value);
            } else {
                throw incompatibleLocation();
            }
        }

        public final int getInt(DynamicObject store, Shape shape) {
            return getInt(store, checkShape(store, shape));
        }

        @Override
        public final boolean canStore(Object value) {
            return value instanceof Integer;
        }

        @Override
        public final void setInt(DynamicObject store, int value, Shape oldShape, Shape newShape) {
            store.setShapeAndGrow(oldShape, newShape);
            setLongInternal(store, value);
        }

        public Class<Integer> getType() {
            return int.class;
        }
    }

    public static class DoubleLocationDecorator extends PrimitiveLocationDecorator implements DoubleLocation {
        private final boolean allowInt;

        public DoubleLocationDecorator(InternalLongLocation longLocation, boolean allowInt) {
            super(longLocation);
            this.allowInt = allowInt;
        }

        @Override
        public final Object get(DynamicObject store, boolean condition) {
            return getDouble(store, condition);
        }

        public double getDouble(DynamicObject store, boolean condition) {
            return Double.longBitsToDouble(getLong(store, condition));
        }

        public void setDouble(DynamicObject store, double value, Shape shape) {
            setLongInternal(store, Double.doubleToRawLongBits(value));
        }

        public void setDouble(DynamicObject store, double value) {
            setDouble(store, value, null);
        }

        @Override
        public final void setInternal(DynamicObject store, Object value) throws IncompatibleLocationException {
            if (canStore(value)) {
                setDouble(store, ((Number) value).doubleValue(), null);
            } else {
                throw incompatibleLocation();
            }
        }

        public final double getDouble(DynamicObject store, Shape shape) {
            return getDouble(store, checkShape(store, shape));
        }

        @Override
        public final boolean canStore(Object value) {
            return value instanceof Double || (allowInt && value instanceof Integer);
        }

        @Override
        public final void setDouble(DynamicObject store, double value, Shape oldShape, Shape newShape) {
            store.setShapeAndGrow(oldShape, newShape);
            setDouble(store, value, newShape);
        }

        public Class<Double> getType() {
            return double.class;
        }
    }

    public static class BooleanLocationDecorator extends PrimitiveLocationDecorator implements BooleanLocation {
        public BooleanLocationDecorator(InternalLongLocation longLocation) {
            super(longLocation);
        }

        @Override
        public final Object get(DynamicObject store, boolean condition) {
            return getBoolean(store, condition);
        }

        public boolean getBoolean(DynamicObject store, boolean condition) {
            return getLong(store, condition) != 0;
        }

        public void setBoolean(DynamicObject store, boolean value, Shape shape) {
            setLongInternal(store, value ? 1 : 0);
        }

        public void setBoolean(DynamicObject store, boolean value) {
            setBoolean(store, value, null);
        }

        @Override
        public final void setInternal(DynamicObject store, Object value) throws IncompatibleLocationException {
            if (canStore(value)) {
                setBoolean(store, (boolean) value, null);
            } else {
                throw incompatibleLocation();
            }
        }

        public final boolean getBoolean(DynamicObject store, Shape shape) {
            return getBoolean(store, checkShape(store, shape));
        }

        @Override
        public final boolean canStore(Object value) {
            return value instanceof Boolean;
        }

        @Override
        public final void setBoolean(DynamicObject store, boolean value, Shape oldShape, Shape newShape) {
            store.setShapeAndGrow(oldShape, newShape);
            setBoolean(store, value, newShape);
        }

        public Class<Boolean> getType() {
            return boolean.class;
        }
    }
}
