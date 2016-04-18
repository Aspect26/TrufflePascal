/*
 * Copyright (c) 2012, 2012, Oracle and/or its affiliates. All rights reserved.
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

import static com.oracle.truffle.api.dsl.test.TestHelper.executeWith;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.test.BinaryNodeTest.BinaryNode;
import com.oracle.truffle.api.dsl.test.PolymorphicTest2Factory.Polymorphic1Factory;
import com.oracle.truffle.api.dsl.test.TypeSystemTest.TestRootNode;
import com.oracle.truffle.api.nodes.NodeCost;

public class PolymorphicTest2 {

    @Test
    public void testMultipleTypes() {
        /* Tests the unexpected polymorphic case. */
        TestRootNode<Polymorphic1> node = TestHelper.createRoot(Polymorphic1Factory.getInstance());
        assertEquals(21, executeWith(node, false, false));
        assertEquals(42, executeWith(node, 21, 21));
        assertEquals("(boolean,int)", executeWith(node, false, 42));
        assertEquals(NodeCost.POLYMORPHIC, node.getNode().getCost());
    }

    @SuppressWarnings("unused")
    abstract static class Polymorphic1 extends BinaryNode {

        @Specialization
        int add(int left, int right) {
            return 42;
        }

        @Specialization
        int add(boolean left, boolean right) {
            return 21;
        }

        @Specialization
        String add(boolean left, int right) {
            return "(boolean,int)";
        }

    }

}
