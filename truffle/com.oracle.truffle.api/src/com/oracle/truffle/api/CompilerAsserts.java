/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api;

/**
 * Assertions about the code produced by the Truffle compiler. All operations have no effect when
 * either executed in the interpreter or in the compiled code. The assertions are checked during
 * code generation and the Truffle compiler produces for failing assertions a stack trace that
 * identifies the code position of the assertion in the context of the current compilation.
 *
 * @since 0.8 or earlier
 */
public final class CompilerAsserts {
    private CompilerAsserts() {
    }

    /**
     * Assertion that this code position should never be reached during compilation. It can be used
     * for exceptional code paths or rare code paths that should never be included in a compilation
     * unit. See {@link CompilerDirectives#transferToInterpreter()} for the corresponding compiler
     * directive.
     * 
     * @since 0.8 or earlier
     */
    public static void neverPartOfCompilation() {
    }

    /**
     * Assertion that this code position should never be reached during compilation. It can be used
     * for exceptional code paths or rare code paths that should never be included in a compilation
     * unit. See {@link CompilerDirectives#transferToInterpreter()} for the corresponding compiler
     * directive.
     *
     * @param message text associated with the bailout exception
     * @since 0.8 or earlier
     */
    public static void neverPartOfCompilation(String message) {
    }

    /**
     * Assertion that the corresponding value is reduced to a constant during compilation.
     *
     * @param value the value that must be constant during compilation
     * @since 0.8 or earlier
     */
    public static <T> void compilationConstant(Object value) {
        if (!CompilerDirectives.isCompilationConstant(value)) {
            neverPartOfCompilation("Value is not compilation constant");
        }
    }

    /**
     * Assertion that the corresponding value is reduced to a constant during the initial partial
     * evaluation phase.
     *
     * @param value the value that must be constant during compilation
     * @since 0.8 or earlier
     */
    public static <T> void partialEvaluationConstant(Object value) {
    }
}
