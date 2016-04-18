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
 * <h3>Passing Arguments</h3>
 *
 * <p>
 * When invoking a call target with {@link CallTarget#call(Object[])}, arguments can be passed. A
 * Truffle node can access the arguments passed into the Truffle method by using
 * {@link VirtualFrame#getArguments}.
 * </p>
 *
 * <p>
 * The arguments class should only contain fields that are declared as final. This allows the
 * Truffle runtime to improve optimizations around guest language method calls. Also, the arguments
 * object array must never be stored into a field. It should be created immediately before invoking
 * {@link CallTarget#call(Object[])} and no longer be accessed afterwards.
 * </p>
 *
 * <p>
 * The next part of the Truffle API introduction is at {@link com.oracle.truffle.api.FrameTest} .
 * </p>
 */
public class ArgumentsTest {

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
        TestRootNode rootNode = new TestRootNode(new TestArgumentNode[]{new TestArgumentNode(0), new TestArgumentNode(1)});
        CallTarget target = runtime.createCallTarget(rootNode);
        Object result = target.call(new Object[]{20, 22});
        Assert.assertEquals(42, result);
    }

    private static class TestRootNode extends RootNode {

        @Children private final TestArgumentNode[] children;

        TestRootNode(TestArgumentNode[] children) {
            super(TestingLanguage.class, null, null);
            this.children = children;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            int sum = 0;
            for (int i = 0; i < children.length; ++i) {
                sum += children[i].execute(frame);
            }
            return sum;
        }
    }

    private static class TestArgumentNode extends Node {

        private final int index;

        TestArgumentNode(int index) {
            this.index = index;
        }

        int execute(VirtualFrame frame) {
            return (Integer) frame.getArguments()[index];
        }
    }
}
