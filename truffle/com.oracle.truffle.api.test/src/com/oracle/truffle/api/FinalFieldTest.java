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
package com.oracle.truffle.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.utilities.InstrumentationTestMode;

/**
 * <h3>Using Final Fields in Node Classes</h3>
 *
 * <p>
 * The usage of final fields in node classes is highly encouraged. It is beneficial for performance
 * to declare every field that is not pointing to a child node as final. This gives the Truffle
 * runtime an increased opportunity to optimize this node.
 * </p>
 *
 * <p>
 * If a node has a value which may change at run time, but will rarely do so, it is recommended to
 * speculate on the field being final. This involves starting executing with a node where this field
 * is final and only if this turns out to be no longer the case, the node is replaced with an
 * alternative implementation of the operation (see {@link ReplaceTest}).
 * </p>
 *
 * <p>
 * The next part of the Truffle API introduction is at {@link com.oracle.truffle.api.ReplaceTest}.
 * </p>
 */
public class FinalFieldTest {

    @Before
    public void before() {
        InstrumentationTestMode.set(true);
    }

    @After
    public void after() {
        InstrumentationTestMode.set(false);
    }

    @Test
    public void test() {
        TruffleRuntime runtime = Truffle.getRuntime();
        TestRootNode rootNode = new TestRootNode(new TestChildNode[]{new TestChildNode(20), new TestChildNode(22)});
        CallTarget target = runtime.createCallTarget(rootNode);
        Object result = target.call();
        Assert.assertEquals(42, result);
    }

    private static class TestRootNode extends RootNode {

        @Children private final TestChildNode[] children;

        TestRootNode(TestChildNode[] children) {
            super(TestingLanguage.class, null, null);
            this.children = children;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            int sum = 0;
            for (int i = 0; i < children.length; ++i) {
                sum += children[i].execute();
            }
            return sum;
        }
    }

    private static class TestChildNode extends Node {

        private final int value;

        TestChildNode(int value) {
            this.value = value;
        }

        public int execute() {
            return value;
        }
    }
}
