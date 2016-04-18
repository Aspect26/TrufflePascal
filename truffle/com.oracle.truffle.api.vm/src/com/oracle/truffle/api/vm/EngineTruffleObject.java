/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api.vm;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import java.io.IOException;
import java.util.Objects;

final class EngineTruffleObject implements TruffleObject, ForeignAccess.Factory {
    private final PolyglotEngine engine;
    private final TruffleObject delegate;

    public EngineTruffleObject(PolyglotEngine engine, TruffleObject obj) {
        this.engine = engine;
        this.delegate = obj;
    }

    @Override
    public ForeignAccess getForeignAccess() {
        return ForeignAccess.create(this);
    }

    TruffleObject getDelegate() {
        return delegate;
    }

    @Override
    public boolean canHandle(TruffleObject obj) {
        return true;
    }

    @Override
    public CallTarget accessMessage(Message tree) {
        return Truffle.getRuntime().createCallTarget(new WrappingRoot(TruffleLanguage.class, tree.createNode()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.engine);
        hash = 89 * hash + Objects.hashCode(this.delegate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof EngineTruffleObject) {
            final EngineTruffleObject other = (EngineTruffleObject) obj;
            return engine == other.engine && Objects.equals(this.delegate, other.delegate);
        }
        return false;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    static class WrappingRoot extends RootNode {
        @Child private Node foreignAccess;

        @SuppressWarnings("rawtypes")
        public WrappingRoot(Class<? extends TruffleLanguage> lang, Node foreignAccess) {
            super(lang, null, null);
            this.foreignAccess = foreignAccess;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            EngineTruffleObject engineTruffleObject = (EngineTruffleObject) ForeignAccess.getReceiver(frame);
            try {
                return engineTruffleObject.engine.invokeForeign(foreignAccess, frame, engineTruffleObject.delegate);
            } catch (IOException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }

}
