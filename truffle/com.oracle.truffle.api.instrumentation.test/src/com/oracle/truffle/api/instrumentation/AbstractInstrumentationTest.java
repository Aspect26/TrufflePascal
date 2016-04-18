/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api.instrumentation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;

/**
 * Base class for instrumentation tests.
 */
public abstract class AbstractInstrumentationTest {

    protected PolyglotEngine engine;

    protected final ByteArrayOutputStream out = new ByteArrayOutputStream();
    protected final ByteArrayOutputStream err = new ByteArrayOutputStream();

    @Before
    public void setup() {
        engine = PolyglotEngine.newBuilder().setOut(out).setErr(err).build();
    }

    protected void assertEnabledInstrument(String id) {
        Assert.assertTrue(engine.getInstruments().get(id).isEnabled());
    }

    protected String run(Source source) throws IOException {
        this.out.reset();
        this.err.reset();
        engine.eval(source);
        this.out.flush();
        this.err.flush();
        String outText = getOut();
        return outText;
    }

    protected static boolean containsTag(String[] tags, String tag) {
        for (int i = 0; i < tags.length; i++) {
            if (tags[i] == tag) {
                return true;
            }
        }
        return false;
    }

    protected String run(String source) throws IOException {
        return run(lines(source));
    }

    protected void assertEvalOut(String source, String output) throws IOException {
        String actual = run(lines(source));
        if (!actual.equals(output)) {
            Assert.assertEquals(output, actual);
        }
    }

    protected final String getOut() {
        return new String(out.toByteArray());
    }

    protected final String getErr() {
        return new String(err.toByteArray());
    }

    protected static Source lines(String... lines) {
        StringBuilder b = new StringBuilder();
        for (String line : lines) {
            b.append(line);
            b.append("\n");
        }
        return Source.fromText(b.toString(), null).withMimeType(InstrumentationTestLanguage.MIME_TYPE);
    }

    @After
    public void teardown() {
        if (engine != null) {
            engine.dispose();
        }
    }
}
