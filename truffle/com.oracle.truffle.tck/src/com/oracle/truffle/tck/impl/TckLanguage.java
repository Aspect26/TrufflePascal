/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.tck.impl;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleLanguage.Env;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrument.Visualizer;
import com.oracle.truffle.api.instrument.WrapperNode;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;

import java.io.IOException;

@TruffleLanguage.Registration(mimeType = "application/x-tck", name = "TCK", version = "1.0")
public final class TckLanguage extends TruffleLanguage<Env> {
    public static final TckLanguage INSTANCE = new TckLanguage();

    @Override
    protected Env createContext(Env env) {
        return env;
    }

    @Override
    protected CallTarget parse(Source code, Node context, String... argumentNames) throws IOException {
        final RootNode root;
        final String txt = code.getCode();
        if (txt.startsWith("TCK42:")) {
            int nextColon = txt.indexOf(":", 6);
            String mimeType = txt.substring(6, nextColon);
            Source toParse = Source.fromText(txt.substring(nextColon + 1), "").withMimeType(mimeType);
            root = new MultiplyNode(toParse);
        } else {
            final double value = Double.parseDouble(txt);
            root = RootNode.createConstantNode(value);
        }
        return Truffle.getRuntime().createCallTarget(root);
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
        throw new IOException();
    }

    private static final class MultiplyNode extends RootNode implements TruffleObject, ForeignAccess.Factory {
        private final Source code;

        MultiplyNode(Source toParse) {
            super(TckLanguage.class, null, null);
            this.code = toParse;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            Env env = TckLanguage.INSTANCE.findContext(TckLanguage.INSTANCE.createFindContextNode());
            if (frame.getArguments().length == 0) {
                return this;
            }
            try {
                CallTarget call = env.parse(code, (String) frame.getArguments()[1], (String) frame.getArguments()[2]);
                return call.call(6, 7);
            } catch (IOException ex) {
                throw new AssertionError("Cannot parse " + code, ex);
            }
        }

        @Override
        public ForeignAccess getForeignAccess() {
            return ForeignAccess.create(this);
        }

        @Override
        public boolean canHandle(TruffleObject obj) {
            return obj instanceof MultiplyNode;
        }

        @Override
        public CallTarget accessMessage(Message tree) {
            if (tree == Message.IS_EXECUTABLE) {
                return Truffle.getRuntime().createCallTarget(RootNode.createConstantNode(Boolean.TRUE));
            } else if (Message.createExecute(2).equals(tree)) {
                return Truffle.getRuntime().createCallTarget(this);
            } else {
                throw UnsupportedMessageException.raise(tree);
            }
        }

    }

    public static Number expectNumber(Object o) {
        if (o instanceof Number) {
            return (Number) o;
        }
        CompilerDirectives.transferToInterpreter();
        throw new IllegalArgumentException(o + " not a Number");
    }

    public static String expectString(Object o) {
        if (o instanceof String) {
            return (String) o;
        }
        CompilerDirectives.transferToInterpreter();
        throw new IllegalArgumentException(o + " not a String");
    }

    public static TruffleObject expectTruffleObject(Object o) {
        if (o instanceof TruffleObject) {
            return (TruffleObject) o;
        }
        CompilerDirectives.transferToInterpreter();
        throw new IllegalArgumentException(o + " not a TruffleObject");
    }

    public static int checkBounds(int idx, int size) {
        if (idx < 0 || idx >= size) {
            CompilerDirectives.transferToInterpreter();
            throw new IndexOutOfBoundsException("Index: " + idx + " Size: " + size);
        }
        return idx;
    }

}
