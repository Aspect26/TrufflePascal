/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
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

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

/**
 * @deprecated package name renamed to {@link com.oracle.truffle.api.profiles.ConditionProfile}
 *             instead
 * @since 0.8 or earlier
 */
@SuppressWarnings("deprecation")
@Deprecated
public final class BinaryConditionProfile extends ConditionProfile {

    @CompilationFinal private boolean wasTrue;
    @CompilationFinal private boolean wasFalse;

    BinaryConditionProfile() {
        /* package protected constructor */
    }

    /** @since 0.8 or earlier */
    @Override
    public boolean profile(boolean value) {
        if (value) {
            if (!wasTrue) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                wasTrue = true;
            }
            return true;
        } else {
            if (!wasFalse) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                wasFalse = true;
            }
            return false;
        }
    }

    /** @since 0.8 or earlier */
    public boolean wasTrue() {
        return wasTrue;
    }

    /** @since 0.8 or earlier */
    public boolean wasFalse() {
        return wasFalse;
    }

    /** @since 0.8 or earlier */
    @Override
    public String toString() {
        return String.format("%s(wasTrue=%s, wasFalse=%s)@%x", getClass().getSimpleName(), wasTrue, wasFalse, hashCode());
    }
}
