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

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.TypeCast;
import com.oracle.truffle.api.dsl.TypeCheck;
import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.dsl.test.TypeSystemErrorsTest.Types1.Type1;
import com.oracle.truffle.api.dsl.test.TypeSystemTest.ValueNode;

public class TypeSystemErrorsTest {

    @TypeSystem({int.class, boolean.class})
    public static class ErrorTypes0 {

    }

    @ExpectError("Invalid type order. The type(s) [java.lang.String] are inherited from a earlier defined type java.lang.CharSequence.")
    @TypeSystem({CharSequence.class, String.class})
    public static class ErrorTypes1 {

    }

    public static class Types1 {
        public static class Type1 {
        }
    }

    public static class Types2 {
        public static class Type1 {
        }
    }

    // verify boxed type overlay
    @ExpectError("Two types result in the same boxed name: Type1.")
    @TypeSystem({Type1.class, com.oracle.truffle.api.dsl.test.TypeSystemErrorsTest.Types2.Type1.class})
    public static class ErrorTypes2 {

    }

    public static class Types3 {
        public static class Object {
        }
    }

    // verify Object name cannot appear
    @ExpectError("Two types result in the same boxed name: Object.")
    @TypeSystem({com.oracle.truffle.api.dsl.test.TypeSystemErrorsTest.Types3.Object.class})
    public static class ErrorTypes3 {
    }

    public static class Types4 {
        public static class Integer {
        }
    }

    // verify int boxed name
    @ExpectError("Two types result in the same boxed name: Integer.")
    @TypeSystem({int.class, com.oracle.truffle.api.dsl.test.TypeSystemErrorsTest.Types4.Integer.class})
    public static class ErrorTypes4 {
    }

    @TypeSystemReference(ErrorTypes0.class)
    @NodeChild
    @ExpectError("The @TypeSystem of the node and the @TypeSystem of the @NodeChild does not match. Types0 != SimpleTypes. ")
    abstract static class ErrorNode1 extends ValueNode {
    }

    @TypeSystem({int.class})
    public static class CastError1 {
        @TypeCast(int.class)
        @ExpectError("The provided return type \"String\" does not match expected return type \"int\".%")
        public static String asInteger(Object value) {
            return (String) value;
        }
    }

    @TypeSystem({int.class})
    public static class CastError2 {
        @TypeCast(int.class)
        @ExpectError("The provided return type \"boolean\" does not match expected return type \"int\".%")
        public static boolean asInteger(Object value) {
            return (boolean) value;
        }
    }

    @TypeSystem({int.class})
    public static class CastError4 {
        @ExpectError("@TypeCast annotated method asInt must be public and static.")
        @TypeCast(int.class)
        public int asInt(Object value) {
            return (int) value;
        }
    }

    @TypeSystem({int.class})
    public static class CastError5 {
        @ExpectError("@TypeCast annotated method asInt must be public and static.")
        @TypeCast(int.class)
        static int asInt(Object value) {
            return (int) value;
        }
    }

    @TypeSystem({int.class})
    public static class CheckError2 {
        @ExpectError("@TypeCheck annotated method isInt must be public and static.")
        @TypeCheck(int.class)
        public boolean isInt(Object value) {
            return value instanceof Integer;
        }
    }

    @TypeSystem({int.class})
    public static class CheckError3 {
        @ExpectError("@TypeCheck annotated method isInt must be public and static.")
        @TypeCheck(int.class)
        static boolean isInt(Object value) {
            return value instanceof Integer;
        }
    }

}
