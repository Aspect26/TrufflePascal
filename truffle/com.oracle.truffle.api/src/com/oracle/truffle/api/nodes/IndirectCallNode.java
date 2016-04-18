/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
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

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Represents an indirect call to a {@link CallTarget}. Indirect calls are calls for which the
 * {@link CallTarget} may change dynamically for each consecutive call. This part of the Truffle API
 * enables the runtime system to perform additional optimizations on indirect calls.
 *
 * Please note: This class is not intended to be sub classed by guest language implementations.
 *
 * @see DirectCallNode for faster calls with a constantly known {@link CallTarget}.
 * @since 0.8 or earlier
 */
public abstract class IndirectCallNode extends Node {
    /**
     * Constructor for implementation subclasses.
     * 
     * @since 0.8 or earlier
     */
    protected IndirectCallNode() {
    }

    /**
     * Performs an indirect call to the given {@link CallTarget} target with the provided arguments.
     *
     * @param frame the caller frame
     * @param target the {@link CallTarget} to call
     * @param arguments the arguments to provide
     * @return the return value of the call
     * @since 0.8 or earlier
     */
    public abstract Object call(VirtualFrame frame, CallTarget target, Object[] arguments);

    /** @since 0.8 or earlier */
    public static IndirectCallNode create() {
        return Truffle.getRuntime().createIndirectCallNode();
    }

}
