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
package com.oracle.truffle.api.utilities;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.Truffle;

/**
 * Holds an {@link Assumption}, and knows how to recreate it with the same properties on
 * invalidation. Used so that mutability is isolated in this class, and all other classes that need
 * an assumption that may be recreated can have a final reference to an object of this class. Note
 * that you should be careful that repeated invalidations do not cause a deoptimization loop in that
 * same way that you would with any other assumption.
 * 
 * @since 0.8 or earlier
 */
public class CyclicAssumption {

    private final String name;
    private volatile Assumption assumption;

    private static final AtomicReferenceFieldUpdater<CyclicAssumption, Assumption> ASSUMPTION_UPDATER = AtomicReferenceFieldUpdater.newUpdater(CyclicAssumption.class, Assumption.class, "assumption");

    /** @since 0.8 or earlier */
    public CyclicAssumption(String name) {
        this.name = name;
        this.assumption = Truffle.getRuntime().createAssumption(name);
    }

    /** @since 0.8 or earlier */
    @TruffleBoundary
    public void invalidate() {
        Assumption newAssumption = Truffle.getRuntime().createAssumption(name);
        Assumption oldAssumption = ASSUMPTION_UPDATER.getAndSet(this, newAssumption);
        oldAssumption.invalidate();
    }

    /** @since 0.8 or earlier */
    public Assumption getAssumption() {
        return assumption;
    }

}
