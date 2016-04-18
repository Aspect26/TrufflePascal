/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api.dsl.test;

import java.math.BigInteger;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.TypeCast;
import com.oracle.truffle.api.dsl.TypeCheck;
import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.dsl.internal.DSLOptions;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

public class TypeSystemTest {

    @TypeSystem({byte.class, short.class, int.class, long.class, double.class, boolean.class, BigInteger.class, String.class, CallTarget.class, BExtendsAbstract.class, CExtendsAbstract.class,
                    Abstract.class, Interface.class, Object[].class})
    @DSLOptions
    static class SimpleTypes {

        static int intCheck;
        static int intCast;

        @TypeCheck(int.class)
        public static boolean isInteger(Object value) {
            intCheck++;
            return value instanceof Integer;
        }

        @TypeCast(int.class)
        public static int asInteger(Object value) {
            intCast++;
            return (int) value;
        }

        @ImplicitCast
        public static double castDouble(int value) {
            return value;
        }

        @ImplicitCast
        public static long castLong(int value) {
            return value;
        }

        @ImplicitCast
        public static BigInteger castBigInteger(int value) {
            return BigInteger.valueOf(value);
        }

    }

    @TypeSystemReference(SimpleTypes.class)
    @GenerateNodeFactory
    public static class ValueNode extends Node {

        public ValueNode() {
        }

        public int executeInt(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectInteger(execute(frame));
        }

        public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectLong(execute(frame));
        }

        public String executeString(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectString(execute(frame));
        }

        public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectBoolean(execute(frame));
        }

        public Object[] executeIntArray(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectObjectArray(execute(frame));
        }

        public BigInteger executeBigInteger(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectBigInteger(execute(frame));
        }

        public BExtendsAbstract executeBExtendsAbstract(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectBExtendsAbstract(execute(frame));
        }

        public CExtendsAbstract executeCExtendsAbstract(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectCExtendsAbstract(execute(frame));
        }

        public Abstract executeAbstract(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectAbstract(execute(frame));
        }

        public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectDouble(execute(frame));
        }

        public Interface executeInterface(VirtualFrame frame) throws UnexpectedResultException {
            return SimpleTypesGen.expectInterface(execute(frame));
        }

        public Object execute(@SuppressWarnings("unused") VirtualFrame frame) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ValueNode copy() {
            return (ValueNode) super.copy();
        }
    }

    @NodeChild(value = "children", type = ValueNode[].class)
    @GenerateNodeFactory
    public abstract static class ChildrenNode extends ValueNode {

    }

    @TypeSystemReference(SimpleTypes.class)
    public static class TestRootNode<E extends ValueNode> extends RootNode {

        @Child private E node;

        public TestRootNode(E node) {
            super(TestingLanguage.class, null, null);
            this.node = node;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            return node.execute(frame);
        }

        public E getNode() {
            return node;
        }
    }

    public static class ArgumentNode extends ValueNode {

        private int invocationCount;
        final int index;

        public ArgumentNode(int index) {
            this.index = index;
        }

        public int getInvocationCount() {
            return invocationCount;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            invocationCount++;
            return frame.getArguments()[index];
        }

        @Override
        public int executeInt(VirtualFrame frame) throws UnexpectedResultException {
            invocationCount++;
            // avoid casts for some tests
            Object o = frame.getArguments()[index];
            if (o instanceof Integer) {
                return (int) o;
            }
            throw new UnexpectedResultException(o);
        }

    }

    abstract static class Abstract {
    }

    static final class BExtendsAbstract extends Abstract {

        static final BExtendsAbstract INSTANCE = new BExtendsAbstract();

    }

    static final class CExtendsAbstract extends Abstract {

        static final CExtendsAbstract INSTANCE = new CExtendsAbstract();
    }

    interface Interface {
    }
}
