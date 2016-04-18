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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleLanguage.Env;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.instrument.Visualizer;
import com.oracle.truffle.api.instrument.WrapperNode;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;

public class ImplicitExplicitExportTest {
    private static Thread mainThread;
    private PolyglotEngine vm;

    @Before
    public void initializeVM() {
        mainThread = Thread.currentThread();
        vm = PolyglotEngine.newBuilder().executor(Executors.newSingleThreadExecutor()).build();
        assertTrue("Found " + L1 + " language", vm.getLanguages().containsKey(L1));
        assertTrue("Found " + L2 + " language", vm.getLanguages().containsKey(L2));
        assertTrue("Found " + L3 + " language", vm.getLanguages().containsKey(L3));
    }

    @After
    public void cleanThread() {
        mainThread = null;
    }

    @Test
    public void explicitExportFound() throws IOException {
        // @formatter:off
        vm.eval(Source.fromText("explicit.ahoj=42", "Fourty two").withMimeType(L1));
        Object ret = vm.eval(
            Source.fromText("return=ahoj", "Return").withMimeType(L3)
        ).get();
        // @formatter:on
        assertEquals("42", ret);
    }

    @Test
    public void implicitExportFound() throws IOException {
        // @formatter:off
        vm.eval(
            Source.fromText("implicit.ahoj=42", "Fourty two").withMimeType(L1)
        );
        Object ret = vm.eval(
            Source.fromText("return=ahoj", "Return").withMimeType(L3)
        ).get();
        // @formatter:on
        assertEquals("42", ret);
    }

    @Test
    public void explicitExportPreferred2() throws IOException {
        // @formatter:off
        vm.eval(
            Source.fromText("implicit.ahoj=42", "Fourty two").withMimeType(L1)
        );
        vm.eval(
            Source.fromText("explicit.ahoj=43", "Fourty three").withMimeType(L2)
        );
        Object ret = vm.eval(
            Source.fromText("return=ahoj", "Return").withMimeType(L3)
        ).get();
        // @formatter:on
        assertEquals("Explicit import from L2 is used", "43", ret);
        assertEquals("Global symbol is also 43", "43", vm.findGlobalSymbol("ahoj").get());
    }

    @Test
    public void explicitExportPreferred1() throws IOException {
        // @formatter:off
        vm.eval(
            Source.fromText("explicit.ahoj=43", "Fourty three").withMimeType(L1)
        );
        vm.eval(
            Source.fromText("implicit.ahoj=42", "Fourty two").withMimeType(L2)
        );
        Object ret = vm.eval(
            Source.fromText("return=ahoj", "Return").withMimeType(L3)
        ).get();
        // @formatter:on
        assertEquals("Explicit import from L2 is used", "43", ret);
        assertEquals("Global symbol is also 43", "43", vm.findGlobalSymbol("ahoj").execute().get());
    }

    static final class Ctx implements TruffleObject {
        static final Set<Ctx> disposed = new HashSet<>();

        final Map<String, String> explicit = new HashMap<>();
        final Map<String, String> implicit = new HashMap<>();
        final Env env;

        Ctx(Env env) {
            this.env = env;
        }

        void dispose() {
            disposed.add(this);
        }

        @Override
        public ForeignAccess getForeignAccess() {
            throw new UnsupportedOperationException();
        }
    }

    private abstract static class AbstractExportImportLanguage extends TruffleLanguage<Ctx> {

        @Override
        protected Ctx createContext(Env env) {
            if (mainThread != null) {
                assertNotEquals("Should run asynchronously", Thread.currentThread(), mainThread);
            }
            return new Ctx(env);
        }

        @Override
        protected void disposeContext(Ctx context) {
            context.dispose();
        }

        @Override
        protected CallTarget parse(Source code, Node context, String... argumentNames) throws IOException {
            if (code.getCode().startsWith("parse=")) {
                throw new IOException(code.getCode().substring(6));
            }
            return new ValueCallTarget(code, this);
        }

        @Override
        protected Object findExportedSymbol(Ctx context, String globalName, boolean onlyExplicit) {
            if (context.explicit.containsKey(globalName)) {
                return context.explicit.get(globalName);
            }
            if (!onlyExplicit && context.implicit.containsKey(globalName)) {
                return context.implicit.get(globalName);
            }
            return null;
        }

        @Override
        protected Object getLanguageGlobal(Ctx context) {
            return context;
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
            return null;
        }

        @Override
        protected Object evalInContext(Source source, Node node, MaterializedFrame mFrame) throws IOException {
            return null;
        }

        private Object importExport(Source code) {
            assertNotEquals("Should run asynchronously", Thread.currentThread(), mainThread);
            final Node node = createFindContextNode();
            Ctx ctx = findContext(node);
            Properties p = new Properties();
            try (Reader r = code.getReader()) {
                p.load(r);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
            Enumeration<Object> en = p.keys();
            while (en.hasMoreElements()) {
                Object n = en.nextElement();
                if (n instanceof String) {
                    String k = (String) n;
                    if (k.startsWith("explicit.")) {
                        ctx.explicit.put(k.substring(9), p.getProperty(k));
                    }
                    if (k.startsWith("implicit.")) {
                        ctx.implicit.put(k.substring(9), p.getProperty(k));
                    }
                    if (k.equals("return")) {
                        return ctx.env.importSymbol(p.getProperty(k));
                    }
                }
            }
            return null;
        }
    }

    private static final class ValueCallTarget implements RootCallTarget {
        private final Source code;
        private final AbstractExportImportLanguage language;

        private ValueCallTarget(Source code, AbstractExportImportLanguage language) {
            this.code = code;
            this.language = language;
        }

        @Override
        public RootNode getRootNode() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object call(Object... arguments) {
            return language.importExport(code);
        }
    }

    public static final String L1 = "application/x-test-import-export-1";
    public static final String L1_ALT = "application/alt-test-import-export-1";
    static final String L2 = "application/x-test-import-export-2";
    static final String L3 = "application/x-test-import-export-3";

    @TruffleLanguage.Registration(mimeType = {L1, L1_ALT}, name = "ImportExport1", version = "0")
    public static final class ExportImportLanguage1 extends AbstractExportImportLanguage {
        public static final AbstractExportImportLanguage INSTANCE = new ExportImportLanguage1();

        public ExportImportLanguage1() {
        }

        @SuppressWarnings("unused")
        // BEGIN: config.read
        @Override
        protected Ctx createContext(Env env) {
            String[] args = (String[]) env.getConfig().get("CMD_ARGS");
            // FINISH: config.read

            return super.createContext(env);
        }

        @Override
        protected String toString(Ctx ctx, Object value) {
            if (value instanceof String) {
                try {
                    int number = Integer.parseInt((String) value);
                    return number + ": Int";
                } catch (NumberFormatException ex) {
                    // go on
                }
            }
            return Objects.toString(value);
        }
    }

    @TruffleLanguage.Registration(mimeType = L2, name = "ImportExport2", version = "0")
    public static final class ExportImportLanguage2 extends AbstractExportImportLanguage {
        public static final AbstractExportImportLanguage INSTANCE = new ExportImportLanguage2();

        public ExportImportLanguage2() {
        }

        @Override
        protected String toString(Ctx ctx, Object value) {
            if (value instanceof String) {
                try {
                    double number = Double.parseDouble((String) value);
                    return number + ": Double";
                } catch (NumberFormatException ex) {
                    // go on
                }
            }
            return Objects.toString(value);
        }
    }

    @TruffleLanguage.Registration(mimeType = L3, name = "ImportExport3", version = "0")
    public static final class ExportImportLanguage3 extends AbstractExportImportLanguage {
        public static final AbstractExportImportLanguage INSTANCE = new ExportImportLanguage3();

        private ExportImportLanguage3() {
        }
    }

}
