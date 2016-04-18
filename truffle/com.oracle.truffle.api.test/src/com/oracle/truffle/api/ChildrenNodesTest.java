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

import java.util.Iterator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.utilities.InstrumentationTestMode;

/**
 * <h3>Creating an Array of Children Nodes</h3>
 *
 * <p>
 * An array of children nodes can be used as a field in a parent node. The field has to be annotated
 * with {@link com.oracle.truffle.api.nodes.Node.Children} and must be declared private and final.
 * Before assigning the field in the parent node constructor, {@link Node#adoptChildren} must be
 * called in order to update the parent pointers in the child nodes. After filling the array with
 * its first values, it must never be changed. It is only possible to call {@link Node#replace} on a
 * child node.
 * </p>
 *
 * <p>
 * The next part of the Truffle API introduction is at {@link com.oracle.truffle.api.FinalFieldTest}
 * .
 * </p>
 */
public class ChildrenNodesTest {

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
        TestChildNode firstChild = new TestChildNode();
        TestChildNode secondChild = new TestChildNode();
        TestRootNode rootNode = new TestRootNode(new TestChildNode[]{firstChild, secondChild});
        CallTarget target = runtime.createCallTarget(rootNode);
        Assert.assertEquals(rootNode, firstChild.getParent());
        Assert.assertEquals(rootNode, secondChild.getParent());
        Iterator<Node> iterator = rootNode.getChildren().iterator();
        Assert.assertEquals(firstChild, iterator.next());
        Assert.assertEquals(secondChild, iterator.next());
        Assert.assertFalse(iterator.hasNext());
        Object result = target.call();
        Assert.assertEquals(42, result);
    }

    class TestRootNode extends RootNode {

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

    class TestChildNode extends Node {

        TestChildNode() {
        }

        public int execute() {
            return 21;
        }
    }

    @Test
    public void testMultipleChildrenFields() {
        TruffleRuntime runtime = Truffle.getRuntime();
        TestChildNode firstChild = new TestChildNode();
        TestChildNode secondChild = new TestChildNode();
        TestChildNode thirdChild = new TestChildNode();
        TestChildNode forthChild = new TestChildNode();
        TestRootNode rootNode = new TestRoot2Node(new TestChildNode[]{firstChild, secondChild}, new TestChildNode[]{thirdChild, forthChild});
        CallTarget target = runtime.createCallTarget(rootNode);
        Assert.assertEquals(rootNode, firstChild.getParent());
        Assert.assertEquals(rootNode, secondChild.getParent());
        Assert.assertEquals(rootNode, thirdChild.getParent());
        Assert.assertEquals(rootNode, forthChild.getParent());
        Iterator<Node> iterator = rootNode.getChildren().iterator();
        Assert.assertEquals(firstChild, iterator.next());
        Assert.assertEquals(secondChild, iterator.next());
        Assert.assertEquals(thirdChild, iterator.next());
        Assert.assertEquals(forthChild, iterator.next());
        Assert.assertFalse(iterator.hasNext());
        Object result = target.call();
        Assert.assertEquals(2 * 42, result);
    }

    class TestRoot2Node extends TestRootNode {
        @Children private final TestChildNode[] children1;
        @Children private final TestChildNode[] children2;

        TestRoot2Node(TestChildNode[] children1, TestChildNode[] children2) {
            super(new TestChildNode[0]);
            this.children1 = children1;
            this.children2 = children2;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            int sum = 0;
            for (int i = 0; i < children1.length; ++i) {
                sum += children1[i].execute();
            }
            for (int i = 0; i < children2.length; ++i) {
                sum += children2[i].execute();
            }
            return sum;
        }
    }
}
