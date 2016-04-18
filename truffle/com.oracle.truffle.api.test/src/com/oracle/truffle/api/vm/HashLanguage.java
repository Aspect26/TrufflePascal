/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api.vm;

import java.io.IOException;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleLanguage.Env;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrument.Visualizer;
import com.oracle.truffle.api.instrument.WrapperNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;

@TruffleLanguage.Registration(name = "Hash", mimeType = "application/x-test-hash", version = "1.0")
public class HashLanguage extends TruffleLanguage<Env> {
    public static final HashLanguage INSTANCE = new HashLanguage();

    @Override
    protected Env createContext(Env env) {
        return env;
    }

    @Override
    protected CallTarget parse(Source code, Node context, String... argumentNames) throws IOException {
        return Truffle.getRuntime().createCallTarget(new HashNode(this, code));
    }

    @Override
    protected Object findExportedSymbol(Env context, String globalName, boolean onlyExplicit) {
        return null;
    }

    @Override
    protected Object getLanguageGlobal(Env context) {
        return null;
    }

    @Override
    protected boolean isObjectOfLanguage(Object object) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Visualizer getVisualizer() {
        return null;
    }

    @Override
    protected boolean isInstrumentable(Node node) {
        return false;
    }

    @Override
    protected WrapperNode createWrapperNode(Node node) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Object evalInContext(Source source, Node node, MaterializedFrame mFrame) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of
                                                                       // generated methods, choose
                                                                       // Tools | Templates.
    }

    private static class HashNode extends RootNode {
        private static int counter;
        private final Source code;
        private final int id;

        HashNode(HashLanguage hash, Source code) {
            super(hash.getClass(), null, null);
            this.code = code;
            id = ++counter;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            return System.identityHashCode(this) + "@" + code.getCode() + " @ " + id;
        }
    }

    @TruffleLanguage.Registration(name = "AltHash", mimeType = "application/x-test-hash-alt", version = "1.0")
    public static final class SubLang extends HashLanguage {
        public static final SubLang INSTANCE = new SubLang();
    }
}
