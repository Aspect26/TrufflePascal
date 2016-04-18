/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api.nodes;

import com.oracle.truffle.api.CompilerDirectives;

/**
 * Represents a rough estimate for the cost of a {@link Node}. This estimate can be used by runtime
 * systems or guest languages to implement heuristics based on Truffle ASTs.
 *
 * @see Node#getCost()
 * @since 0.8 or earlier
 */
public enum NodeCost {

    /**
     * This node has literally no costs and should be ignored for heuristics. This is particularly
     * useful for wrapper and profiling nodes which should not influence the heuristics.
     * 
     * @since 0.8 or earlier
     */
    NONE,

    /**
     * This node has a {@link CompilerDirectives#transferToInterpreter()} or
     * {@link CompilerDirectives#transferToInterpreterAndInvalidate()} as its first unconditional
     * statement.
     * 
     * @since 0.8 or earlier
     */
    UNINITIALIZED,

    /**
     * This node represents a specialized monomorphic version of an operation.
     * 
     * @since 0.8 or earlier
     */
    MONOMORPHIC,

    /**
     * This node represents a polymorphic version of an operation. For multiple chained polymorphic
     * nodes the first may return {@link #MONOMORPHIC} and all additional nodes should return
     * {@link #POLYMORPHIC}.
     * 
     * @since 0.8 or earlier
     */
    POLYMORPHIC,

    /**
     * This node represents a megamorphic version of an operation. This value should only be used if
     * the operation implementation supports monomorphism and polymorphism otherwise
     * {@link #MONOMORPHIC} should be used instead.
     * 
     * @since 0.8 or earlier
     */
    MEGAMORPHIC;

    /** @since 0.8 or earlier */
    public boolean isTrivial() {
        return this == NONE || this == UNINITIALIZED;
    }
}
