/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;

import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameInstance;
import com.oracle.truffle.api.frame.FrameInstance.FrameAccess;
import com.oracle.truffle.api.frame.FrameInstanceVisitor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;

public class StackTraceTest {

    @Test
    public void testNoStackTrace() {
        StackTrace stack = new StackTrace();
        Assert.assertNull(stack.callerFrame);
        Assert.assertNull(stack.currentFrame);
        Assert.assertEquals(0, stack.frames.size());
    }

    @Test
    public void testSingleStackTrace() {
        CallTarget callTarget = createCallTarget(new ReturnStackTraceNode());
        StackTrace stack = (StackTrace) callTarget.call();

        Assert.assertEquals(1, stack.frames.size());
        Assert.assertSame(callTarget, stack.currentFrame.getCallTarget());
        Assert.assertNull(stack.currentFrame.getCallNode());
        assertInvariants(stack);
    }

    @Test
    public void testDirectStackTrace() {
        CallTarget createStackTrace = createCallTarget(new ReturnStackTraceNode());
        CallTarget call = createCallTarget(new TestCallWithDirectTargetNode(createStackTrace));
        StackTrace stack = (StackTrace) call.call();

        assertInvariants(stack);
        Assert.assertEquals(2, stack.frames.size());
        Assert.assertSame(createStackTrace, stack.currentFrame.getCallTarget());
        Assert.assertNull(stack.currentFrame.getCallNode());
        Assert.assertSame(call, stack.callerFrame.getCallTarget());
        Assert.assertSame(findCallNode(call), stack.callerFrame.getCallNode());
    }

    @Test
    public void testIndirectStackTrace() {
        CallTarget createStackTrace = createCallTarget(new ReturnStackTraceNode());
        CallTarget call = createCallTarget(new TestCallWithIndirectTargetNode(createStackTrace));
        StackTrace stack = (StackTrace) call.call();

        Assert.assertEquals(2, stack.frames.size());
        Assert.assertSame(createStackTrace, stack.currentFrame.getCallTarget());
        Assert.assertNull(stack.currentFrame.getCallNode());
        Assert.assertSame(call, stack.callerFrame.getCallTarget());
        Assert.assertSame(findCallNode(call), stack.callerFrame.getCallNode());
        assertInvariants(stack);
    }

    @Test
    public void testCallTargetStackTrace() {
        CallTarget createStackTrace = createCallTarget(new ReturnStackTraceNode());
        CallTarget call = createCallTarget(new TestCallWithCallTargetNode(createStackTrace));
        StackTrace stack = (StackTrace) call.call();

        assertInvariants(stack);
        Assert.assertEquals(2, stack.frames.size());
        Assert.assertSame(createStackTrace, stack.currentFrame.getCallTarget());
        Assert.assertNull(stack.currentFrame.getCallNode());
        Assert.assertSame(call, stack.callerFrame.getCallTarget());
        Assert.assertNull(stack.callerFrame.getCallNode());
    }

    @Test
    public void testCombinedStackTrace() {
        CallTarget createStackTrace = createCallTarget(new ReturnStackTraceNode());
        CallTarget callTarget = createCallTarget(new TestCallWithCallTargetNode(createStackTrace));
        CallTarget indirect = createCallTarget(new TestCallWithIndirectTargetNode(callTarget));
        CallTarget direct = createCallTarget(new TestCallWithDirectTargetNode(indirect));
        StackTrace stack = (StackTrace) direct.call();

        assertInvariants(stack);
        Assert.assertEquals(4, stack.frames.size());

        Assert.assertSame(createStackTrace, stack.currentFrame.getCallTarget());
        Assert.assertNull(stack.currentFrame.getCallNode());
        Assert.assertSame(callTarget, stack.callerFrame.getCallTarget());
        Assert.assertNull(stack.callerFrame.getCallNode());

        Assert.assertSame(indirect, stack.frames.get(2).getCallTarget());
        Assert.assertSame(findCallNode(indirect), stack.frames.get(2).getCallNode());

        Assert.assertSame(direct, stack.frames.get(3).getCallTarget());
        Assert.assertSame(findCallNode(direct), stack.frames.get(3).getCallNode());
    }

    @Test
    public void testFrameAccess() {
        CallTarget callTarget = createCallTarget(new TestCallWithCallTargetNode(null));
        CallTarget indirect = createCallTarget(new TestCallWithIndirectTargetNode(callTarget));
        CallTarget direct = createCallTarget(new TestCallWithDirectTargetNode(indirect));
        CallTarget test = createCallTarget(new TestCallNode(null) {
            @Override
            Object execute(VirtualFrame frame) {
                Truffle.getRuntime().iterateFrames(new FrameInstanceVisitor<Object>() {
                    public Object visitFrame(FrameInstance frameInstance) {
                        Assert.assertNull(frameInstance.getFrame(FrameAccess.NONE, true));

                        Frame readOnlyFrame = frameInstance.getFrame(FrameAccess.READ_ONLY, true);
                        FrameSlot slot = readOnlyFrame.getFrameDescriptor().findFrameSlot("demo");
                        Assert.assertEquals(42, readOnlyFrame.getValue(slot));

                        Frame readWriteFrame = frameInstance.getFrame(FrameAccess.READ_WRITE, true);
                        Assert.assertEquals(42, readWriteFrame.getValue(slot));
                        readWriteFrame.setObject(slot, 43);

                        Frame materializedFrame = frameInstance.getFrame(FrameAccess.MATERIALIZE, true);
                        Assert.assertEquals(43, materializedFrame.getValue(slot));

                        materializedFrame.setObject(slot, 44);
                        Assert.assertEquals(44, readOnlyFrame.getValue(slot));
                        Assert.assertEquals(44, readWriteFrame.getValue(slot));

                        return null;
                    }
                });
                return null;
            }
        });
        findTestCallNode(callTarget).setNext(test);
        direct.call();
    }

    @Test
    public void testStackTraversal() {
        CallTarget callTarget = createCallTarget(new TestCallWithCallTargetNode(null));
        CallTarget indirect = createCallTarget(new TestCallWithIndirectTargetNode(callTarget));
        CallTarget direct = createCallTarget(new TestCallWithDirectTargetNode(indirect));
        CallTarget test = createCallTarget(new TestCallNode(null) {
            int visitCount = 0;

            @Override
            Object execute(VirtualFrame frame) {
                Object result = Truffle.getRuntime().iterateFrames(new FrameInstanceVisitor<Object>() {
                    public Object visitFrame(FrameInstance frameInstance) {
                        visitCount++;
                        return "foobar";
                    }
                });
                Assert.assertEquals(1, visitCount);
                Assert.assertEquals("foobar", result);

                visitCount = 0;
                result = Truffle.getRuntime().iterateFrames(new FrameInstanceVisitor<Object>() {
                    public Object visitFrame(FrameInstance frameInstance) {
                        visitCount++;
                        if (visitCount == 2) {
                            return "foobar";
                        } else {
                            return null; // continue traversing
                        }
                    }
                });
                Assert.assertEquals(2, visitCount);
                Assert.assertEquals("foobar", result);

                return null;
            }
        });
        findTestCallNode(callTarget).setNext(test);
        direct.call();
    }

    @Test
    public void testAsynchronousFrameAccess() throws InterruptedException, ExecutionException, TimeoutException {
        final ExecutorService exec = Executors.newFixedThreadPool(50);
        try {
            List<Callable<Void>> callables = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                callables.add(new Callable<Void>() {
                    @Override
                    public Void call() {
                        final CallTarget createStackTrace = createCallTarget(new ReturnStackTraceNode());
                        final CallTarget callTarget = createCallTarget(new TestCallWithCallTargetNode(createStackTrace));
                        final CallTarget indirect = createCallTarget(new TestCallWithIndirectTargetNode(callTarget));
                        final CallTarget direct = createCallTarget(new TestCallWithDirectTargetNode(indirect));

                        for (int j = 0; j < 10; j++) {
                            StackTrace stack = (StackTrace) direct.call();
                            assertInvariants(stack);
                            Assert.assertEquals(4, stack.frames.size());
                            Assert.assertSame(createStackTrace, stack.currentFrame.getCallTarget());
                            Assert.assertNull(stack.currentFrame.getCallNode());
                            Assert.assertSame(callTarget, stack.callerFrame.getCallTarget());
                            Assert.assertNull(stack.callerFrame.getCallNode());

                            Assert.assertSame(indirect, stack.frames.get(2).getCallTarget());
                            Assert.assertSame(findCallNode(indirect), stack.frames.get(2).getCallNode());

                            Assert.assertSame(direct, stack.frames.get(3).getCallTarget());
                            Assert.assertSame(findCallNode(direct), stack.frames.get(3).getCallNode());
                        }
                        return null;
                    }
                });
            }
            for (Future<?> future : exec.invokeAll(callables)) {
                future.get(5000, TimeUnit.MILLISECONDS);
            }

        } finally {
            exec.shutdown();
        }

    }

    private static TestCallNode findTestCallNode(CallTarget target) {
        return ((TestRootNode) ((RootCallTarget) target).getRootNode()).callNode;
    }

    private static Node findCallNode(CallTarget target) {
        return findTestCallNode(target).getCallNode();
    }

    private static void assertInvariants(StackTrace stack) {
        if (stack.frames.size() == 0) {
            Assert.assertNull(stack.currentFrame);
        } else {
            Assert.assertNotNull(stack.currentFrame);
        }

        if (stack.frames.size() <= 1) {
            Assert.assertNull(stack.callerFrame);
        } else {
            Assert.assertNotNull(stack.callerFrame);
        }

        for (int i = 0; i < stack.frames.size(); i++) {
            FrameInstance frame = stack.frames.get(i);
            if (i == 0) {
                assertFrameEquals(stack.currentFrame, frame);
            } else if (i == 1) {
                assertFrameEquals(stack.callerFrame, frame);
            }
            Assert.assertNotNull(frame.getCallTarget());
            Assert.assertNotNull(frame.toString()); // # does not crash
        }
    }

    private static void assertFrameEquals(FrameInstance expected, FrameInstance other) {
        Assert.assertEquals(expected.isVirtualFrame(), other.isVirtualFrame());
        Assert.assertSame(expected.getCallNode(), other.getCallNode());
        Assert.assertSame(expected.getCallTarget(), other.getCallTarget());
    }

    private static CallTarget createCallTarget(TestCallNode callNode) {
        return Truffle.getRuntime().createCallTarget(new TestRootNode(callNode));
    }

    private static class TestCallWithCallTargetNode extends TestCallNode {

        TestCallWithCallTargetNode(CallTarget next) {
            super(next);
        }

        @Override
        Object execute(VirtualFrame frame) {
            return next.call();
        }

    }

    private static class TestCallWithIndirectTargetNode extends TestCallNode {

        @Child IndirectCallNode indirectCall = Truffle.getRuntime().createIndirectCallNode();

        TestCallWithIndirectTargetNode(CallTarget next) {
            super(next);
        }

        @Override
        Object execute(VirtualFrame frame) {
            return indirectCall.call(frame, next, new Object[0]);
        }

        @Override
        public Node getCallNode() {
            return indirectCall;
        }

    }

    private static class TestCallWithDirectTargetNode extends TestCallNode {

        @Child DirectCallNode directCall;

        TestCallWithDirectTargetNode(CallTarget next) {
            super(next);
        }

        @Override
        Object execute(VirtualFrame frame) {
            if (directCall == null || directCall.getCallTarget() != next) {
                directCall = insert(Truffle.getRuntime().createDirectCallNode(next));
            }
            return directCall.call(frame, new Object[0]);
        }

        @Override
        public Node getCallNode() {
            return directCall;
        }

    }

    private static class ReturnStackTraceNode extends TestCallNode {

        ReturnStackTraceNode() {
            super(null);
        }

        @Override
        Object execute(VirtualFrame frame) {
            return new StackTrace();
        }
    }

    private static class StackTrace {

        final List<FrameInstance> frames;
        final FrameInstance currentFrame;
        final FrameInstance callerFrame;

        StackTrace() {
            frames = new ArrayList<>();
            Truffle.getRuntime().iterateFrames(new FrameInstanceVisitor<Void>() {
                public Void visitFrame(FrameInstance frameInstance) {
                    frames.add(frameInstance);
                    return null;
                }
            });

            currentFrame = Truffle.getRuntime().getCurrentFrame();
            callerFrame = Truffle.getRuntime().getCallerFrame();
        }

    }

    private abstract static class TestCallNode extends Node {

        protected CallTarget next;

        TestCallNode(CallTarget next) {
            this.next = next;
        }

        public void setNext(CallTarget next) {
            this.next = next;
        }

        abstract Object execute(VirtualFrame frame);

        public Node getCallNode() {
            return null;
        }

    }

    private static class TestRootNode extends RootNode {

        @Child private TestCallNode callNode;

        TestRootNode(TestCallNode callNode) {
            super(TestingLanguage.INSTANCE.getClass(), null, null);
            this.callNode = callNode;
            getFrameDescriptor().addFrameSlot("demo");
        }

        @Override
        public Object execute(VirtualFrame frame) {
            frame.setObject(getFrameDescriptor().findFrameSlot("demo"), 42);
            return callNode.execute(frame);
        }

    }

}
