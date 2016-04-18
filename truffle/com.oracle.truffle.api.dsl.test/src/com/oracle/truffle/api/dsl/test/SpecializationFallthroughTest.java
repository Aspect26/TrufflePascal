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

import static com.oracle.truffle.api.dsl.test.TestHelper.array;
import static com.oracle.truffle.api.dsl.test.TestHelper.assertRuns;

import org.junit.Assert;
import org.junit.Test;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.test.SpecializationFallthroughTestFactory.FallthroughTest0Factory;
import com.oracle.truffle.api.dsl.test.SpecializationFallthroughTestFactory.FallthroughTest1Factory;
import com.oracle.truffle.api.dsl.test.SpecializationFallthroughTestFactory.FallthroughTest2Factory;
import com.oracle.truffle.api.dsl.test.SpecializationFallthroughTestFactory.FallthroughTest3Factory;
import com.oracle.truffle.api.dsl.test.SpecializationFallthroughTestFactory.FallthroughTest4Factory;
import com.oracle.truffle.api.dsl.test.SpecializationFallthroughTestFactory.FallthroughTest5Factory;
import com.oracle.truffle.api.dsl.test.TestHelper.ExecutionListener;
import com.oracle.truffle.api.dsl.test.TypeSystemTest.TestRootNode;
import com.oracle.truffle.api.dsl.test.TypeSystemTest.ValueNode;

public class SpecializationFallthroughTest {

    @Test
    public void testFallthrough0() {
        assertRuns(FallthroughTest0Factory.getInstance(), //
                        array(0, 0, 1, 2), //
                        array(0, 0, 1, 2), //
                        new ExecutionListener() {
                            public void afterExecution(TestRootNode<? extends ValueNode> node, int index, Object value, Object expectedResult, Object actualResult, boolean last) {
                                if (!last) {
                                    return;
                                }
                                if (FallthroughTest0.fallthroughCount > 1) {
                                    Assert.fail("The fallthrough case must never be triggered twice. Therfore count must be <= 1, but is not.");
                                }
                            }
                        });
    }

    @NodeChildren({@NodeChild("a")})
    static class FallthroughTest0 extends ValueNode {

        static int fallthroughCount = 0;

        FallthroughTest0() {
            fallthroughCount = 0;
        }

        @Specialization(rewriteOn = ArithmeticException.class)
        int do1(int a) throws ArithmeticException {
            if (a == 0) {
                fallthroughCount++;
                throw new ArithmeticException();
            }
            return a;
        }

        @Fallback
        Object doFallback(Object a) {
            return a;
        }
    }

    /*
     * Tests that the fall through is never triggered twice for monomorphic cases.
     */
    @Test
    public void testFallthrough1() {
        assertRuns(FallthroughTest1Factory.getInstance(), //
                        array(0, 0, 0, 1, 2), //
                        array(0, 0, 0, 1, 2), //
                        new ExecutionListener() {
                            public void afterExecution(TestRootNode<? extends ValueNode> node, int index, Object value, Object expectedResult, Object actualResult, boolean last) {
                                if (!last) {
                                    return;
                                }
                                if (FallthroughTest1.fallthroughCount > 1) {
                                    Assert.fail("The fallthrough case must never be triggered twice. Therfore count must be <= 1, but is not.");
                                }
                            }
                        });
    }

    /* TODO assert falltrough do1 before do2 */
    @NodeChildren({@NodeChild("a")})
    static class FallthroughTest1 extends ValueNode {

        static int fallthroughCount;

        FallthroughTest1() {
            fallthroughCount = 0;
        }

        @Specialization(rewriteOn = ArithmeticException.class)
        int do1(int a) throws ArithmeticException {
            if (a == 0) {
                fallthroughCount++;
                throw new ArithmeticException();
            }
            return a;
        }

        @Specialization
        int do2(int a) {
            return a;
        }

    }

    /*
     * Tests that the fall through is never triggered twice with two falltrhoughs in one operation.
     */
    @Test
    public void testFallthrough2() {
        assertRuns(FallthroughTest2Factory.getInstance(), //
                        array(0, 0, 1, 1, 2, 2), //
                        array(0, 0, 1, 1, 2, 2), //
                        new ExecutionListener() {
                            public void afterExecution(TestRootNode<? extends ValueNode> node, int index, Object value, Object expectedResult, Object actualResult, boolean last) {
                                if (!last) {
                                    return;
                                }
                                if (FallthroughTest2.fallthrough1 > 1) {
                                    Assert.fail();
                                }
                                if (FallthroughTest2.fallthrough2 > 1) {
                                    Assert.fail();
                                }
                                FallthroughTest2.fallthrough1 = 0;
                                FallthroughTest2.fallthrough2 = 0;
                            }
                        });
    }

    @NodeChildren({@NodeChild("a")})
    static class FallthroughTest2 extends ValueNode {

        static int fallthrough1;
        static int fallthrough2;

        @Specialization(rewriteOn = ArithmeticException.class)
        int do1(int a) throws ArithmeticException {
            if (a == 0) {
                fallthrough1++;
                throw new ArithmeticException();
            }
            return a;
        }

        @Specialization(rewriteOn = ArithmeticException.class)
        int do2(int a) throws ArithmeticException {
            if (a == 1) {
                fallthrough2++;
                throw new ArithmeticException();
            }
            return a;
        }

        @Specialization
        int do3(int a) {
            return a;
        }
    }

    /*
     * Tests that the fall through is never triggered twice. In this case mixed fallthrough with
     * normal specializations.
     */
    @Test
    public void testFallthrough3() {
        assertRuns(FallthroughTest3Factory.getInstance(), //
                        array(0, 0, 1, 1, 2, 2), //
                        array(0, 0, 1, 1, 2, 2), //
                        new ExecutionListener() {
                            public void afterExecution(TestRootNode<? extends ValueNode> node, int index, Object value, Object expectedResult, Object actualResult, boolean last) {
                                if (!last) {
                                    return;
                                }
                                if (FallthroughTest3.fallthrough1 > 1) {
                                    Assert.fail(String.valueOf(FallthroughTest3.fallthrough1));
                                }
                                FallthroughTest3.fallthrough1 = 0;
                            }
                        });
    }

    @NodeChildren({@NodeChild("a")})
    static class FallthroughTest3 extends ValueNode {

        static int fallthrough1;

        boolean guard0(int a) {
            return a == 1;
        }

        @Specialization(guards = "guard0(a)")
        int do2(int a) {
            return a;
        }

        @Specialization(rewriteOn = ArithmeticException.class)
        int do1(int a) throws ArithmeticException {
            if (a == 0) {
                fallthrough1++;
                throw new ArithmeticException();
            }
            return a;
        }

        @Specialization
        int do3(int a) {
            return a;
        }

    }

    @Test
    public void testFallthrough4() {
        assertRuns(FallthroughTest4Factory.getInstance(), //
                        array(0, 0, 1, 1, 2, 2), //
                        array(0, 0, 1, 1, 2, 2), //
                        new ExecutionListener() {
                            public void afterExecution(TestRootNode<? extends ValueNode> node, int index, Object value, Object expectedResult, Object actualResult, boolean last) {
                                if (!last) {
                                    return;
                                }
                                if (FallthroughTest4.fallthrough1 > 1) {
                                    Assert.fail(String.valueOf(FallthroughTest4.fallthrough1));
                                }
                                if (FallthroughTest4.fallthrough2 > 1) {
                                    Assert.fail(String.valueOf(FallthroughTest4.fallthrough1));
                                }
                                FallthroughTest4.fallthrough1 = 0;
                                FallthroughTest4.fallthrough2 = 0;
                            }
                        });
    }

    @NodeChildren({@NodeChild("a")})
    static class FallthroughTest4 extends ValueNode {

        static int fallthrough1;
        static int fallthrough2;

        @Specialization(rewriteOn = ArithmeticException.class)
        int do1(int a) throws ArithmeticException {
            if (a == 0) {
                fallthrough1++;
                throw new ArithmeticException();
            }
            return a;
        }

        @Specialization(rewriteOn = ArithmeticException.class)
        int do2(int a) throws ArithmeticException {
            if (a == 1) {
                fallthrough2++;
                throw new ArithmeticException();
            }
            return a;
        }

        @Specialization
        int do3(int a) {
            return a;
        }

    }

    @Test
    public void testFallthrough5() {
        assertRuns(FallthroughTest5Factory.getInstance(), //
                        array(0, 0, 1, 1, 2, 2), //
                        array(0, 0, 1, 1, 2, 2), //
                        new ExecutionListener() {
                            public void afterExecution(TestRootNode<? extends ValueNode> node, int index, Object value, Object expectedResult, Object actualResult, boolean last) {
                                if (!last) {
                                    return;
                                }
                                if (FallthroughTest5.fallthrough1 > 1) {
                                    Assert.fail(String.valueOf(FallthroughTest5.fallthrough1));
                                }
                                FallthroughTest5.fallthrough1 = 0;
                            }
                        });
    }

    @NodeChildren({@NodeChild("a")})
    static class FallthroughTest5 extends ValueNode {

        static int fallthrough1;

        @Specialization(guards = "isDo1(a)", rewriteOn = ArithmeticException.class)
        int do1(int a) throws ArithmeticException {
            if (a == 0) {
                fallthrough1++;
                throw new ArithmeticException();
            }
            return a;
        }

        protected static boolean isDo1(int a) {
            return a == 0 || a == 1;
        }

        @Specialization(guards = "isDo1(a)")
        int do2(int a) {
            return a;
        }

        @Specialization
        int do3(int a) {
            return a;
        }

    }

    /* Throwing RuntimeExceptions without rewriteOn is allowed. */
    @NodeChildren({@NodeChild("a")})
    static class FallthroughExceptionType0 extends ValueNode {

        @Specialization
        int do4(int a) throws RuntimeException {
            return a;
        }

    }

    /* Non runtime exceptions must be verified. */
    @NodeChildren({@NodeChild("a")})
    static class FallthroughExceptionType1 extends ValueNode {

        @ExpectError("A rewriteOn checked exception was specified but not thrown in the method's throws clause. The @Specialization method must specify a throws clause with the exception type 'java.lang.Throwable'.")
        @Specialization(rewriteOn = Throwable.class)
        int do4(int a) {
            return a;
        }

    }

    /* Checked exception must be verified. */
    @NodeChildren({@NodeChild("a")})
    static class FallthroughExceptionType2 extends ValueNode {

        @ExpectError("A checked exception 'java.lang.Throwable' is thrown but is not specified using the rewriteOn property. "
                        + "Checked exceptions that are not used for rewriting are not handled by the DSL. Use RuntimeExceptions for this purpose instead.")
        @Specialization
        int do4(int a) throws Throwable {
            return a;
        }

    }

}
