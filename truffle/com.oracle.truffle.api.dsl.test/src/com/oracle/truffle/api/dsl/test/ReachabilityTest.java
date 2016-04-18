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

import java.math.BigInteger;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.test.TypeSystemTest.Abstract;
import com.oracle.truffle.api.dsl.test.TypeSystemTest.BExtendsAbstract;
import com.oracle.truffle.api.dsl.test.TypeSystemTest.ValueNode;

public class ReachabilityTest {

    static class Reachability1 extends ValueNode {
        @Specialization
        int do2() {
            return 2;
        }

        @ExpectError("Specialization is not reachable. It is shadowed by do2().")
        @Specialization
        int do1() {
            return 2;
        }
    }

    @NodeChildren({@NodeChild("a")})
    static class ReachabilityType1 extends ValueNode {
        @Specialization
        int do2(int a) {
            return a;
        }

        @ExpectError("Specialization is not reachable. It is shadowed by do2(int).")
        @Specialization
        int do1(int a) {
            return a;
        }
    }

    @NodeChildren({@NodeChild("a")})
    static class ReachabilityType2 extends ValueNode {
        @Specialization
        BExtendsAbstract do2(BExtendsAbstract a) {
            return a;
        }

        @ExpectError("Specialization is not reachable. It is shadowed by do2(BExtendsAbstract).")
        @Specialization
        BExtendsAbstract do1(BExtendsAbstract a) {
            return a;
        }
    }

    @NodeChildren({@NodeChild("a")})
    static class ReachabilityType3 extends ValueNode {
        @Specialization
        Abstract do2(Abstract a) {
            return a;
        }

        @ExpectError("Specialization is not reachable. It is shadowed by do2(Abstract).")
        @Specialization
        BExtendsAbstract do1(BExtendsAbstract a) {
            return a;
        }
    }

    @NodeChildren({@NodeChild("a")})
    static class ReachabilityType4 extends ValueNode {

        @Specialization
        BExtendsAbstract do2(BExtendsAbstract a) {
            return a;
        }

        @Specialization
        Abstract do1(Abstract a) {
            return a;
        }

    }

    @NodeChildren({@NodeChild("a")})
    static class ReachabilityType5 extends ValueNode {

        @Specialization
        double do2(double a) {
            return a;
        }

        @ExpectError("Specialization is not reachable. It is shadowed by do2(double).")
        @Specialization
        int do1(int a) {
            return a;
        }

    }

    @NodeChildren({@NodeChild("a")})
    static class ReachabilityType6 extends ValueNode {

        @Specialization
        BigInteger do2(BigInteger a) {
            return a;
        }

        @ExpectError("Specialization is not reachable. It is shadowed by do2(BigInteger).")
        @Specialization
        int do1(int a) {
            return a;
        }

    }

    @NodeChildren({@NodeChild("a")})
    static class ReachabilityType7 extends ValueNode {

        @Specialization
        int do2(int a) {
            return a;
        }

        @Specialization
        BigInteger do1(BigInteger a) {
            return a;
        }

    }

    @NodeChildren({@NodeChild("a")})
    static class ReachabilityType8 extends ValueNode {

        @Specialization
        int do2(int a) {
            return a;
        }

        @Specialization
        Object do1(Object a) {
            return a;
        }

    }

    @NodeChildren({@NodeChild("a")})
    static class ReachabilityType9 extends ValueNode {

        @Specialization
        Object do2(Object a) {
            return a;
        }

        @ExpectError("Specialization is not reachable. It is shadowed by do2(Object).")
        @Specialization
        int do1(int a) {
            return a;
        }
    }

    static class ReachabilityGuard1 extends ValueNode {

        boolean foo() {
            return false;
        }

        @Specialization(guards = "foo()")
        int do2() {
            return 1;
        }

        @Specialization
        int do1() {
            return 2;
        }

    }

    static class ReachabilityGuard2 extends ValueNode {

        boolean foo() {
            return false;
        }

        @Specialization
        int do2() {
            return 2;
        }

        @ExpectError("Specialization is not reachable. It is shadowed by do2().")
        @Specialization(guards = "foo()")
        int do1() {
            return 1;
        }

    }

    static class ReachabilityGuard3 extends ValueNode {

        boolean foo() {
            return false;
        }

        @Specialization(guards = "foo()")
        int do2() {
            return 1;
        }

        @Specialization
        int do1() {
            return 2;
        }

    }

    static class ReachabilityGuard4 extends ValueNode {

        boolean foo() {
            return false;
        }

        @Specialization(guards = "foo()")
        int do2() {
            return 1;
        }

        @ExpectError("Specialization is not reachable. It is shadowed by do2().")
        @Specialization(guards = "foo()")
        int do1() {
            return 2;
        }

    }

    static class ReachabilityThrowable1 extends ValueNode {

        @Specialization(rewriteOn = RuntimeException.class)
        int do2() throws RuntimeException {
            return 1;
        }

        @Specialization
        int do1() {
            return 2;
        }

    }

    static class ReachabilityThrowable2 extends ValueNode {

        @Specialization
        int do2() {
            return 1;
        }

        @ExpectError("Specialization is not reachable. It is shadowed by do2().")
        @Specialization(rewriteOn = RuntimeException.class)
        int do1() throws RuntimeException {
            return 2;
        }

    }

}
