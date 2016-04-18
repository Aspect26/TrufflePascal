/*
 * Copyright (c) 2012, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
package com.oracle.truffle.api.nodes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.oracle.truffle.api.nodes.Node.Child;
import com.oracle.truffle.api.nodes.Node.Children;
import com.oracle.truffle.api.nodes.NodeFieldAccessor.NodeFieldKind;

/**
 * Information about a {@link Node} class. A single instance of this class is allocated for every
 * subclass of {@link Node} that is used.
 */
final class NodeClassImpl extends NodeClass {
    private static final NodeFieldAccessor[] EMPTY_NODE_FIELD_ARRAY = new NodeFieldAccessor[0];

    // The comprehensive list of all fields.
    private final NodeFieldAccessor[] fields;
    // Separate arrays for the frequently accessed fields.
    private final NodeFieldAccessor parentField;
    private final NodeFieldAccessor nodeClassField;
    private final NodeFieldAccessor[] childFields;
    private final NodeFieldAccessor[] childrenFields;
    private final NodeFieldAccessor[] cloneableFields;

    private final Class<? extends Node> clazz;

    NodeClassImpl(Class<? extends Node> clazz) {
        super(clazz);
        if (!Node.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException();
        }

        List<NodeFieldAccessor> fieldsList = new ArrayList<>();
        NodeFieldAccessor parentFieldTmp = null;
        NodeFieldAccessor nodeClassFieldTmp = null;
        List<NodeFieldAccessor> childFieldList = new ArrayList<>();
        List<NodeFieldAccessor> childrenFieldList = new ArrayList<>();
        List<NodeFieldAccessor> cloneableFieldList = new ArrayList<>();

        try {
            Field field = Node.class.getDeclaredField("parent");
            assert Node.class.isAssignableFrom(field.getType());
            parentFieldTmp = NodeFieldAccessor.create(NodeFieldKind.PARENT, field);
            field = Node.class.getDeclaredField("nodeClass");
            assert NodeClass.class.isAssignableFrom(field.getType());
            nodeClassFieldTmp = NodeFieldAccessor.create(NodeFieldKind.NODE_CLASS, field);
        } catch (NoSuchFieldException e) {
            throw new AssertionError("Node field not found", e);
        }

        collectInstanceFields(clazz, fieldsList, childFieldList, childrenFieldList, cloneableFieldList);

        this.fields = fieldsList.toArray(EMPTY_NODE_FIELD_ARRAY);
        this.nodeClassField = nodeClassFieldTmp;
        this.parentField = parentFieldTmp;
        this.childFields = childFieldList.toArray(EMPTY_NODE_FIELD_ARRAY);
        this.childrenFields = childrenFieldList.toArray(EMPTY_NODE_FIELD_ARRAY);
        this.cloneableFields = cloneableFieldList.toArray(EMPTY_NODE_FIELD_ARRAY);
        this.clazz = clazz;
    }

    private static void collectInstanceFields(Class<? extends Object> clazz, List<NodeFieldAccessor> fieldsList, List<NodeFieldAccessor> childFieldList, List<NodeFieldAccessor> childrenFieldList,
                    List<NodeFieldAccessor> cloneableFieldList) {
        if (clazz.getSuperclass() != null) {
            collectInstanceFields(clazz.getSuperclass(), fieldsList, childFieldList, childrenFieldList, cloneableFieldList);
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                continue;
            }

            NodeFieldAccessor nodeField;
            if (field.getDeclaringClass() == Node.class && (field.getName().equals("parent") || field.getName().equals("nodeClass"))) {
                continue;
            } else if (field.getAnnotation(Child.class) != null) {
                checkChildField(field);
                nodeField = NodeFieldAccessor.create(NodeFieldKind.CHILD, field);
                childFieldList.add(nodeField);
            } else if (field.getAnnotation(Children.class) != null) {
                checkChildrenField(field);
                nodeField = NodeFieldAccessor.create(NodeFieldKind.CHILDREN, field);
                childrenFieldList.add(nodeField);
            } else {
                nodeField = NodeFieldAccessor.create(NodeFieldKind.DATA, field);
                if (NodeCloneable.class.isAssignableFrom(field.getType())) {
                    cloneableFieldList.add(nodeField);
                }
            }
            fieldsList.add(nodeField);
        }
    }

    @Override
    public NodeFieldAccessor getNodeClassField() {
        return nodeClassField;
    }

    @Override
    public NodeFieldAccessor[] getCloneableFields() {
        return cloneableFields;
    }

    private static boolean isNodeType(Class<?> clazz) {
        return Node.class.isAssignableFrom(clazz) || (clazz.isInterface() && NodeInterface.class.isAssignableFrom(clazz));
    }

    private static void checkChildField(Field field) {
        if (!isNodeType(field.getType())) {
            throw new AssertionError("@Child field type must be a subclass of Node or an interface extending NodeInterface (" + field + ")");
        }
        if (Modifier.isFinal(field.getModifiers())) {
            throw new AssertionError("@Child field must not be final (" + field + ")");
        }
    }

    private static void checkChildrenField(Field field) {
        if (!(field.getType().isArray() && isNodeType(field.getType().getComponentType()))) {
            throw new AssertionError("@Children field type must be an array of a subclass of Node or an interface extending NodeInterface (" + field + ")");
        }
        if (!Modifier.isFinal(field.getModifiers())) {
            throw new AssertionError("@Children field must be final (" + field + ")");
        }
    }

    @Override
    public NodeFieldAccessor[] getFields() {
        return fields;
    }

    @Override
    public NodeFieldAccessor getParentField() {
        return parentField;
    }

    @Override
    public NodeFieldAccessor[] getChildFields() {
        return childFields;
    }

    @Override
    public NodeFieldAccessor[] getChildrenFields() {
        return childrenFields;
    }

    @Override
    public int hashCode() {
        return clazz.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NodeClassImpl) {
            NodeClassImpl other = (NodeClassImpl) obj;
            return clazz.equals(other.clazz);
        }
        return false;
    }

    @Override
    public Iterator<Node> makeIterator(Node node) {
        assert clazz.isInstance(node);
        return new NodeIterator(this, node);
    }

    @Override
    public Class<? extends Node> getType() {
        return clazz;
    }

    private static final class NodeIterator implements Iterator<Node> {
        private final NodeFieldAccessor[] childFields;
        private final NodeFieldAccessor[] childrenFields;
        private final Node node;
        private final int childrenCount;
        private int index;

        protected NodeIterator(NodeClassImpl nodeClass, Node node) {
            this.childFields = nodeClass.getChildFields();
            this.childrenFields = nodeClass.getChildrenFields();
            this.node = node;
            this.childrenCount = childrenCount();
            this.index = 0;
        }

        private int childrenCount() {
            int nodeCount = childFields.length;
            for (NodeFieldAccessor childrenField : childrenFields) {
                Object[] children = ((Object[]) childrenField.getObject(node));
                if (children != null) {
                    nodeCount += children.length;
                }
            }
            return nodeCount;
        }

        private Node nodeAt(int idx) {
            int nodeCount = childFields.length;
            if (idx < nodeCount) {
                return (Node) childFields[idx].getObject(node);
            } else {
                for (NodeFieldAccessor childrenField : childrenFields) {
                    Object[] nodeArray = (Object[]) childrenField.getObject(node);
                    if (idx < nodeCount + nodeArray.length) {
                        return (Node) nodeArray[idx - nodeCount];
                    }
                    nodeCount += nodeArray.length;
                }
            }
            return null;
        }

        private void forward() {
            if (index < childrenCount) {
                index++;
            }
        }

        public boolean hasNext() {
            return index < childrenCount;
        }

        public Node next() {
            try {
                return nodeAt(index);
            } finally {
                forward();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
