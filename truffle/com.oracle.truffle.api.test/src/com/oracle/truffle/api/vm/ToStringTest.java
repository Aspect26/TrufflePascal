/*
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
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

import org.junit.Test;

import com.oracle.truffle.api.source.Source;

public class ToStringTest {
    @Test
    public void valueToStringValueWith1() throws Exception {
        PolyglotEngine engine = PolyglotEngine.newBuilder().build();
        PolyglotEngine.Language language1 = engine.getLanguages().get("application/x-test-import-export-1");
        PolyglotEngine.Language language2 = engine.getLanguages().get("application/x-test-import-export-2");
        language2.eval(Source.fromText("explicit.value=42", "define 42"));
        PolyglotEngine.Value value = language1.eval(Source.fromText("return=value", "42.value"));
        assertEquals("It's fourtytwo", "42", value.get());

        String textual = value.as(String.class);
        assertEquals("Nicely formated as by L1", "42: Int", textual);
    }

    @Test
    public void valueToStringValueWith2() throws Exception {
        PolyglotEngine engine = PolyglotEngine.newBuilder().build();
        PolyglotEngine.Language language1 = engine.getLanguages().get("application/x-test-import-export-1");
        PolyglotEngine.Language language2 = engine.getLanguages().get("application/x-test-import-export-2");
        language1.eval(Source.fromText("explicit.value=42", "define 42"));
        PolyglotEngine.Value value = language2.eval(Source.fromText("return=value", "42.value"));
        assertEquals("It's fourtytwo", "42", value.get());

        String textual = value.as(String.class);
        assertEquals("Nicely formated as by L2", "42.0: Double", textual);
    }

}
