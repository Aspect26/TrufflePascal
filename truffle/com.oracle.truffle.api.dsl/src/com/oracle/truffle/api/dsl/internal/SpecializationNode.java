/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api.dsl.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.Callable;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.dsl.internal.SlowPathEvent.SlowPathEvent0;
import com.oracle.truffle.api.dsl.internal.SlowPathEvent.SlowPathEvent1;
import com.oracle.truffle.api.dsl.internal.SlowPathEvent.SlowPathEvent2;
import com.oracle.truffle.api.dsl.internal.SlowPathEvent.SlowPathEvent3;
import com.oracle.truffle.api.dsl.internal.SlowPathEvent.SlowPathEvent4;
import com.oracle.truffle.api.dsl.internal.SlowPathEvent.SlowPathEvent5;
import com.oracle.truffle.api.dsl.internal.SlowPathEvent.SlowPathEventN;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.InvalidAssumptionException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.NodeUtil;

/**
 * Internal implementation dependent base class for generated specialized nodes.
 */
@NodeInfo(cost = NodeCost.NONE)
public abstract class SpecializationNode extends Node {

    @Child protected SpecializationNode next;

    private final int index;

    public SpecializationNode(int index) {
        this.index = index;
    }

    @Override
    public final NodeCost getCost() {
        return NodeCost.NONE;
    }

    public void reset() {
        SpecializationNode start = findStart();
        SpecializationNode end = findEnd();
        if (start != end) {
            start.replace(end, "reset specialization");
        }
    }

    public static Node updateRoot(Node node) {
        updateRootImpl(((SpecializedNode) node).getSpecializationNode(), node);
        return node;
    }

    private static void updateRootImpl(SpecializationNode start, Node node) {
        start.setRoot(node);
        if (start.next != null) {
            updateRootImpl(start.next, node);
        }
    }

    protected final SpecializationNode polymorphicMerge(SpecializationNode newNode, SpecializationNode merged) {
        if (merged == newNode && count() <= 2) {
            return removeSame(new SlowPathEvent0(this, "merged polymorphic to monomorphic", null));
        }
        return merged;
    }

    public final NodeCost getNodeCost() {
        switch (count()) {
            case 0:
            case 1:
                return NodeCost.UNINITIALIZED;
            case 2:
                return NodeCost.MONOMORPHIC;
            default:
                return NodeCost.POLYMORPHIC;
        }
    }

    protected abstract void setRoot(Node root);

    protected abstract Node[] getSuppliedChildren();

    protected SpecializationNode merge(SpecializationNode newNode, Frame frame) {
        if (isIdentical(newNode, frame)) {
            return this;
        }
        return next != null ? next.merge(newNode, frame) : newNode;
    }

    protected SpecializationNode merge(SpecializationNode newNode, Frame frame, Object o1) {
        if (isIdentical(newNode, frame, o1)) {
            return this;
        }
        return next != null ? next.merge(newNode, frame, o1) : newNode;
    }

    protected SpecializationNode merge(SpecializationNode newNode, Frame frame, Object o1, Object o2) {
        if (isIdentical(newNode, frame, o1, o2)) {
            return this;
        }
        return next != null ? next.merge(newNode, frame, o1, o2) : newNode;
    }

    protected SpecializationNode merge(SpecializationNode newNode, Frame frame, Object o1, Object o2, Object o3) {
        if (isIdentical(newNode, frame, o1, o2, o3)) {
            return this;
        }
        return next != null ? next.merge(newNode, frame, o1, o2, o3) : newNode;
    }

    protected SpecializationNode merge(SpecializationNode newNode, Frame frame, Object o1, Object o2, Object o3, Object o4) {
        if (isIdentical(newNode, frame, o1, o2, o3, o4)) {
            return this;
        }
        return next != null ? next.merge(newNode, frame, o1, o2, o3, o4) : newNode;
    }

    protected SpecializationNode merge(SpecializationNode newNode, Frame frame, Object o1, Object o2, Object o3, Object o4, Object o5) {
        if (isIdentical(newNode, frame, o1, o2, o3, o4, o5)) {
            return this;
        }
        return next != null ? next.merge(newNode, frame, o1, o2, o3, o4, o5) : newNode;
    }

    protected SpecializationNode merge(SpecializationNode newNode, Frame frame, Object... args) {
        if (isIdentical(newNode, frame, args)) {
            return this;
        }
        return next != null ? next.merge(newNode, frame, args) : newNode;
    }

    protected boolean isSame(SpecializationNode other) {
        return getClass() == other.getClass();
    }

    @SuppressWarnings("unused")
    protected boolean isIdentical(SpecializationNode newNode, Frame frame) {
        return isSame(newNode);
    }

    @SuppressWarnings("unused")
    protected boolean isIdentical(SpecializationNode newNode, Frame frame, Object o1) {
        return isSame(newNode);
    }

    @SuppressWarnings("unused")
    protected boolean isIdentical(SpecializationNode newNode, Frame frame, Object o1, Object o2) {
        return isSame(newNode);
    }

    @SuppressWarnings("unused")
    protected boolean isIdentical(SpecializationNode newNode, Frame frame, Object o1, Object o2, Object o3) {
        return isSame(newNode);
    }

    @SuppressWarnings("unused")
    protected boolean isIdentical(SpecializationNode newNode, Frame frame, Object o1, Object o2, Object o3, Object o4) {
        return isSame(newNode);
    }

    @SuppressWarnings("unused")
    protected boolean isIdentical(SpecializationNode newNode, Frame frame, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return isSame(newNode);
    }

    @SuppressWarnings("unused")
    protected boolean isIdentical(SpecializationNode newNode, Frame frame, Object... args) {
        return isSame(newNode);
    }

    protected final int countSame(SpecializationNode node) {
        return findStart().countSameImpl(node);
    }

    private int countSameImpl(SpecializationNode node) {
        if (next != null) {
            return next.countSameImpl(node) + (isSame(node) ? 1 : 0);
        } else {
            return 0;
        }
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof SpecializationNode) {
            return ((SpecializationNode) obj).isSame(this);
        }
        return super.equals(obj);
    }

    @Override
    public final int hashCode() {
        return index;
    }

    private int count() {
        return next != null ? next.count() + 1 : 1;
    }

    private SpecializationNode findEnd() {
        SpecializationNode node = this;
        while (node.next != null) {
            node = node.next;
        }
        return node;
    }

    protected final Object removeThis(final CharSequence reason, Frame frame) {
        return removeThisImpl(reason).acceptAndExecute(frame);
    }

    protected final Object removeThis(final CharSequence reason, Frame frame, Object o1) {
        return removeThisImpl(reason).acceptAndExecute(frame, o1);
    }

    protected final Object removeThis(final CharSequence reason, Frame frame, Object o1, Object o2) {
        return removeThisImpl(reason).acceptAndExecute(frame, o1, o2);
    }

    protected final Object removeThis(final CharSequence reason, Frame frame, Object o1, Object o2, Object o3) {
        return removeThisImpl(reason).acceptAndExecute(frame, o1, o2, o3);
    }

    protected final Object removeThis(final CharSequence reason, Frame frame, Object o1, Object o2, Object o3, Object o4) {
        return removeThisImpl(reason).acceptAndExecute(frame, o1, o2, o3, o4);
    }

    protected final Object removeThis(final CharSequence reason, Frame frame, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return removeThisImpl(reason).acceptAndExecute(frame, o1, o2, o3, o4, o5);
    }

    protected final Object removeThis(final CharSequence reason, Frame frame, Object... args) {
        return removeThisImpl(reason).acceptAndExecute(frame, args);
    }

    private SpecializationNode removeThisImpl(final CharSequence reason) {
        this.replace(this.next, reason);
        return findEnd().findStart();
    }

    protected final SpecializationNode removeSame(final CharSequence reason) {
        SpecializationNode start = SpecializationNode.this.findStart();
        SpecializationNode current = start;
        while (current != null) {
            if (current.isSame(SpecializationNode.this)) {
                NodeUtil.nonAtomicReplace(current, current.next, reason);
                if (current == start) {
                    start = start.next;
                }
            }
            current = current.next;
        }
        return SpecializationNode.this.findEnd().findStart();
    }

    /** Find the topmost of the specialization chain. */
    private SpecializationNode findStart() {
        SpecializationNode node = this;
        Node parent = this.getParent();
        while (parent instanceof SpecializationNode) {
            SpecializationNode parentCast = ((SpecializationNode) parent);
            if (parentCast.next != node) {
                break;
            }
            node = parentCast;
            parent = node.getParent();
        }
        return node;
    }

    private Node findRoot() {
        return findStart().getParent();
    }

    @SuppressWarnings("unused")
    public Object acceptAndExecute(Frame frame) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public Object acceptAndExecute(Frame frame, Object o1) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public Object acceptAndExecute(Frame frame, Object o1, Object o2) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public Object acceptAndExecute(Frame frame, Object o1, Object o2, Object o3) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public Object acceptAndExecute(Frame frame, Object o1, Object o2, Object o3, Object o4) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public Object acceptAndExecute(Frame frame, Object o1, Object o2, Object o3, Object o4, Object o5) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public Object acceptAndExecute(Frame frame, Object... args) {
        throw new UnsupportedOperationException();
    }

    protected SpecializationNode createFallback() {
        return null;
    }

    protected SpecializationNode createPolymorphic() {
        return null;
    }

    @SuppressWarnings("unused")
    protected SpecializationNode createNext(Frame frame) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    protected SpecializationNode createNext(Frame frame, Object o1) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    protected SpecializationNode createNext(Frame frame, Object o1, Object o2) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    protected SpecializationNode createNext(Frame frame, Object o1, Object o2, Object o3) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    protected SpecializationNode createNext(Frame frame, Object o1, Object o2, Object o3, Object o4) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    protected SpecializationNode createNext(Frame frame, Object o1, Object o2, Object o3, Object o4, Object o5) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    protected SpecializationNode createNext(Frame frame, Object... args) {
        throw new UnsupportedOperationException();
    }

    protected final Object uninitialized(Frame frame) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        SpecializationNode newNode = atomic(new InsertionEvent0(this, "insert new specialization", frame));
        if (newNode == null) {
            return unsupported(frame);
        }
        return newNode.acceptAndExecute(frame);
    }

    protected final Object uninitialized(Frame frame, Object o1) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        SpecializationNode newNode = atomic(new InsertionEvent1(this, "insert new specialization", frame, o1));
        if (newNode == null) {
            return unsupported(frame, o1);
        }
        return newNode.acceptAndExecute(frame, o1);
    }

    protected final Object uninitialized(Frame frame, Object o1, Object o2) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        SpecializationNode newNode = atomic(new InsertionEvent2(this, "insert new specialization", frame, o1, o2));
        if (newNode == null) {
            return unsupported(frame, o1, o2);
        }
        return newNode.acceptAndExecute(frame, o1, o2);
    }

    protected final Object uninitialized(Frame frame, Object o1, Object o2, Object o3) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        SpecializationNode newNode = atomic(new InsertionEvent3(this, "insert new specialization", frame, o1, o2, o3));
        if (newNode == null) {
            return unsupported(frame, o1, o2, o3);
        }
        return newNode.acceptAndExecute(frame, o1, o2, o3);
    }

    protected final Object uninitialized(Frame frame, Object o1, Object o2, Object o3, Object o4) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        SpecializationNode newNode = atomic(new InsertionEvent4(this, "insert new specialization", frame, o1, o2, o3, o4));
        if (newNode == null) {
            return unsupported(frame, o1, o2, o3, o4);
        }
        return newNode.acceptAndExecute(frame, o1, o2, o3, o4);
    }

    protected final Object uninitialized(Frame frame, Object o1, Object o2, Object o3, Object o4, Object o5) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        SpecializationNode newNode = atomic(new InsertionEvent5(this, "insert new specialization", frame, o1, o2, o3, o4, o5));
        if (newNode == null) {
            return unsupported(frame, o1, o2, o3, o4, o5);
        }
        return newNode.acceptAndExecute(frame, o1, o2, o3, o4, o5);
    }

    protected final Object uninitialized(Frame frame, Object... args) {
        CompilerDirectives.transferToInterpreterAndInvalidate();
        SpecializationNode newNode = atomic(new InsertionEventN(this, "insert new specialization", frame, args));
        if (newNode == null) {
            return unsupported(frame, args);
        }
        return newNode.acceptAndExecute(frame, args);
    }

    protected final Object remove(String reason, Frame frame) {
        return atomic(new RemoveEvent0(this, reason, frame)).acceptAndExecute(frame);
    }

    protected final Object remove(String reason, Frame frame, Object o1) {
        return atomic(new RemoveEvent1(this, reason, frame, o1)).acceptAndExecute(frame, o1);
    }

    protected final Object remove(String reason, Frame frame, Object o1, Object o2) {
        return atomic(new RemoveEvent2(this, reason, frame, o1, o2)).acceptAndExecute(frame, o1, o2);
    }

    protected final Object remove(String reason, Frame frame, Object o1, Object o2, Object o3) {
        return atomic(new RemoveEvent3(this, reason, frame, o1, o2, o3)).acceptAndExecute(frame, o1, o2, o3);
    }

    protected final Object remove(String reason, Frame frame, Object o1, Object o2, Object o3, Object o4) {
        return atomic(new RemoveEvent4(this, reason, frame, o1, o2, o3, o4)).acceptAndExecute(frame, o1, o2, o3, o4);
    }

    protected final Object remove(String reason, Frame frame, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return atomic(new RemoveEvent5(this, reason, frame, o1, o2, o3, o4, o5)).acceptAndExecute(frame, o1, o2, o3, o4, o5);
    }

    protected final Object remove(String reason, Frame frame, Object... args) {
        return atomic(new RemoveEventN(this, reason, frame, args)).acceptAndExecute(frame, args);
    }

    @SuppressWarnings("unused")
    protected Object unsupported(Frame frame) {
        throw new UnsupportedSpecializationException(findRoot(), getSuppliedChildren());
    }

    @SuppressWarnings("unused")
    protected Object unsupported(Frame frame, Object o1) {
        throw new UnsupportedSpecializationException(findRoot(), getSuppliedChildren(), o1);
    }

    @SuppressWarnings("unused")
    protected Object unsupported(Frame frame, Object o1, Object o2) {
        throw new UnsupportedSpecializationException(findRoot(), getSuppliedChildren(), o1, o2);
    }

    @SuppressWarnings("unused")
    protected Object unsupported(Frame frame, Object o1, Object o2, Object o3) {
        throw new UnsupportedSpecializationException(findRoot(), getSuppliedChildren(), o1, o2, o3);
    }

    @SuppressWarnings("unused")
    protected Object unsupported(Frame frame, Object o1, Object o2, Object o3, Object o4) {
        throw new UnsupportedSpecializationException(findRoot(), getSuppliedChildren(), o1, o2, o3, o4);
    }

    @SuppressWarnings("unused")
    protected Object unsupported(Frame frame, Object o1, Object o2, Object o3, Object o4, Object o5) {
        throw new UnsupportedSpecializationException(findRoot(), getSuppliedChildren(), o1, o2, o3, o4, o5);
    }

    @SuppressWarnings("unused")
    protected Object unsupported(Frame frame, Object... args) {
        throw new UnsupportedSpecializationException(findRoot(), getSuppliedChildren(), args);
    }

    static SpecializationNode insertSorted(SpecializationNode start, final SpecializationNode generated, final CharSequence message, final SpecializationNode merged) {
        if (merged == generated) {
            // new node
            if (start.count() == 2) {
                SpecializationNode polymorphic = start.createPolymorphic();
                /*
                 * For nodes with all parameters evaluated in the execute method we do not need a
                 * polymorphic node. the generated code returns null in createPolymorphic in this
                 * case.
                 */
                if (polymorphic != null) {
                    insertAt(start, polymorphic, "insert polymorphic");
                }
            }
            SpecializationNode current = start;
            while (current != null && current.index < generated.index) {
                current = current.next;
            }
            return insertAt(current, generated, message);
        } else {
            // existing node
            return start;
        }
    }

    static <T> SpecializationNode insertAt(SpecializationNode node, SpecializationNode insertBefore, CharSequence reason) {
        insertBefore.next = node;
        // always guaranteed to be executed inside of an atomic block
        return NodeUtil.nonAtomicReplace(node, insertBefore, reason);
    }

    @Override
    public final String toString() {
        Class<?> clazz = getClass();
        StringBuilder b = new StringBuilder();
        b.append(clazz.getSimpleName());

        appendFields(b, clazz);
        if (next != null) {
            b.append("\n -> ").append(next.toString());
        }
        return b.toString();
    }

    private void appendFields(StringBuilder b, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            return;
        }
        b.append("(");
        String sep = "";
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            b.append(sep);
            String name = field.getName();
            if (name.equals("root")) {
                continue;
            }
            b.append(field.getName());
            b.append(" = ");
            try {
                field.setAccessible(true);
                Object value = field.get(this);
                if (value instanceof Object[]) {
                    b.append(Arrays.toString((Object[]) field.get(this)));
                } else {
                    b.append(field.get(this));
                }
            } catch (IllegalArgumentException e) {
                b.append(e.toString());
            } catch (IllegalAccessException e) {
                b.append(e.toString());
            }
            sep = ", ";
        }
        b.append(")");
    }

    protected static void check(Assumption assumption) throws InvalidAssumptionException {
        if (assumption != null) {
            assumption.check();
        }
    }

    @ExplodeLoop
    protected static void check(Assumption[] assumptions) throws InvalidAssumptionException {
        if (assumptions != null) {
            CompilerAsserts.compilationConstant(assumptions.length);
            for (Assumption assumption : assumptions) {
                check(assumption);
            }
        }
    }

    protected static boolean isValid(Assumption assumption) {
        if (assumption != null) {
            return assumption.isValid();
        }
        return true;
    }

    protected static boolean isValid(Assumption[] assumptions) {
        if (assumptions != null) {
            for (Assumption assumption : assumptions) {
                if (!isValid(assumption)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static final class InsertionEvent0 extends SlowPathEvent0 implements Callable<SpecializationNode> {

        InsertionEvent0(SpecializationNode source, String reason, Frame frame) {
            super(source, reason, frame);
        }

        public SpecializationNode call() throws Exception {
            SpecializationNode next = source.createNext(frame);
            if (next == null) {
                next = source.createFallback();
            }
            if (next == null) {
                return null;
            }
            SpecializationNode start = source.findStart();
            if (start.index == Integer.MAX_VALUE) {
                return insertAt(start, next, this);
            } else {
                return insertSorted(start, next, this, start.merge(next, frame));
            }
        }

    }

    private static final class InsertionEvent1 extends SlowPathEvent1 implements Callable<SpecializationNode> {

        InsertionEvent1(SpecializationNode source, String reason, Frame frame, Object o1) {
            super(source, reason, frame, o1);
        }

        public SpecializationNode call() throws Exception {
            SpecializationNode next = source.createNext(frame, o1);
            if (next == null) {
                next = source.createFallback();
            }
            if (next == null) {
                return null;
            }
            SpecializationNode start = source.findStart();
            if (start.index == Integer.MAX_VALUE) {
                return insertAt(start, next, this);
            } else {
                return insertSorted(start, next, this, start.merge(next, frame, o1));
            }
        }

    }

    private static final class InsertionEvent2 extends SlowPathEvent2 implements Callable<SpecializationNode> {

        InsertionEvent2(SpecializationNode source, String reason, Frame frame, Object o1, Object o2) {
            super(source, reason, frame, o1, o2);
        }

        public SpecializationNode call() throws Exception {
            SpecializationNode next = source.createNext(frame, o1, o2);
            if (next == null) {
                next = source.createFallback();
            }
            if (next == null) {
                return null;
            }
            SpecializationNode start = source.findStart();
            if (start.index == Integer.MAX_VALUE) {
                return insertAt(start, next, this);
            } else {
                return insertSorted(start, next, this, start.merge(next, frame, o1, o2));
            }
        }

    }

    private static final class InsertionEvent3 extends SlowPathEvent3 implements Callable<SpecializationNode> {

        InsertionEvent3(SpecializationNode source, String reason, Frame frame, Object o1, Object o2, Object o3) {
            super(source, reason, frame, o1, o2, o3);
        }

        public SpecializationNode call() throws Exception {
            SpecializationNode next = source.createNext(frame, o1, o2, o3);
            if (next == null) {
                next = source.createFallback();
            }
            if (next == null) {
                return null;
            }
            SpecializationNode start = source.findStart();
            if (start.index == Integer.MAX_VALUE) {
                return insertAt(start, next, this);
            } else {
                return insertSorted(start, next, this, start.merge(next, frame, o1, o2, o3));
            }
        }

    }

    private static final class InsertionEvent4 extends SlowPathEvent4 implements Callable<SpecializationNode> {

        InsertionEvent4(SpecializationNode source, String reason, Frame frame, Object o1, Object o2, Object o3, Object o4) {
            super(source, reason, frame, o1, o2, o3, o4);
        }

        public SpecializationNode call() throws Exception {
            SpecializationNode next = source.createNext(frame, o1, o2, o3, o4);
            if (next == null) {
                next = source.createFallback();
            }
            if (next == null) {
                return null;
            }
            SpecializationNode start = source.findStart();
            if (start.index == Integer.MAX_VALUE) {
                return insertAt(start, next, this);
            } else {
                return insertSorted(start, next, this, start.merge(next, frame, o1, o2, o3, o4));
            }
        }

    }

    private static final class InsertionEvent5 extends SlowPathEvent5 implements Callable<SpecializationNode> {

        InsertionEvent5(SpecializationNode source, String reason, Frame frame, Object o1, Object o2, Object o3, Object o4, Object o5) {
            super(source, reason, frame, o1, o2, o3, o4, o5);
        }

        public SpecializationNode call() throws Exception {
            SpecializationNode next = source.createNext(frame, o1, o2, o3, o4, o5);
            if (next == null) {
                next = source.createFallback();
            }
            if (next == null) {
                return null;
            }
            SpecializationNode start = source.findStart();
            if (start.index == Integer.MAX_VALUE) {
                return insertAt(start, next, this);
            } else {
                return insertSorted(start, next, this, start.merge(next, frame, o1, o2, o3, o4, o5));
            }
        }

    }

    private static final class InsertionEventN extends SlowPathEventN implements Callable<SpecializationNode> {

        InsertionEventN(SpecializationNode source, String reason, Frame frame, Object[] args) {
            super(source, reason, frame, args);
        }

        public SpecializationNode call() throws Exception {
            SpecializationNode next = source.createNext(frame, args);
            if (next == null) {
                next = source.createFallback();
            }
            if (next == null) {
                return null;
            }
            SpecializationNode start = source.findStart();
            if (start.index == Integer.MAX_VALUE) {
                return insertAt(start, next, this);
            } else {
                return insertSorted(start, next, this, start.merge(next, frame, args));
            }
        }
    }

    private static final class RemoveEvent0 extends SlowPathEvent0 implements Callable<SpecializationNode> {

        RemoveEvent0(SpecializationNode source, String reason, Frame frame) {
            super(source, reason, frame);
        }

        public SpecializationNode call() throws Exception {
            return source.removeSame(this);
        }

    }

    private static final class RemoveEvent1 extends SlowPathEvent1 implements Callable<SpecializationNode> {

        RemoveEvent1(SpecializationNode source, String reason, Frame frame, Object o1) {
            super(source, reason, frame, o1);
        }

        public SpecializationNode call() throws Exception {
            return source.removeSame(this);
        }

    }

    private static final class RemoveEvent2 extends SlowPathEvent2 implements Callable<SpecializationNode> {

        RemoveEvent2(SpecializationNode source, String reason, Frame frame, Object o1, Object o2) {
            super(source, reason, frame, o1, o2);
        }

        public SpecializationNode call() throws Exception {
            return source.removeSame(this);
        }

    }

    private static final class RemoveEvent3 extends SlowPathEvent3 implements Callable<SpecializationNode> {

        RemoveEvent3(SpecializationNode source, String reason, Frame frame, Object o1, Object o2, Object o3) {
            super(source, reason, frame, o1, o2, o3);
        }

        public SpecializationNode call() throws Exception {
            return source.removeSame(this);
        }

    }

    private static final class RemoveEvent4 extends SlowPathEvent4 implements Callable<SpecializationNode> {

        RemoveEvent4(SpecializationNode source, String reason, Frame frame, Object o1, Object o2, Object o3, Object o4) {
            super(source, reason, frame, o1, o2, o3, o4);
        }

        public SpecializationNode call() throws Exception {
            return source.removeSame(this);
        }

    }

    private static final class RemoveEvent5 extends SlowPathEvent5 implements Callable<SpecializationNode> {

        RemoveEvent5(SpecializationNode source, String reason, Frame frame, Object o1, Object o2, Object o3, Object o4, Object o5) {
            super(source, reason, frame, o1, o2, o3, o4, o5);
        }

        public SpecializationNode call() throws Exception {
            return source.removeSame(this);
        }

    }

    private static final class RemoveEventN extends SlowPathEventN implements Callable<SpecializationNode> {

        RemoveEventN(SpecializationNode source, String reason, Frame frame, Object[] args) {
            super(source, reason, frame, args);
        }

        public SpecializationNode call() throws Exception {
            return source.removeSame(this);
        }
    }

}
