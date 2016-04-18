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
package com.oracle.truffle.api.instrumentation.examples;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.EventContext;
import com.oracle.truffle.api.instrumentation.ExecutionEventNode;
import com.oracle.truffle.api.instrumentation.ExecutionEventNodeFactory;
import com.oracle.truffle.api.instrumentation.InstrumentationTestLanguage;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;
import com.oracle.truffle.api.instrumentation.TruffleInstrument.Registration;
import com.oracle.truffle.api.source.SourceSection;

/**
 * Example for simple and optimize able version of an expression coverage instrumentation. Parts
 * that are already covered or have never been instrumented can be optimized without peak
 * performance overhead.
 *
 * Covered statements are printed to the instrumentation stream which should demonstrate an
 * alternate way of communication from the instrumentation to the user.
 */
@Registration(id = CoverageExample.ID)
public final class CoverageExample extends TruffleInstrument {

    public static final String ID = "test-coverage";

    private final Set<SourceSection> coverage = new HashSet<>();

    @Override
    protected void onCreate(final Env env) {
        final PrintStream out = new PrintStream(env.out());
        env.getInstrumenter().attachFactory(SourceSectionFilter.newBuilder().tagIs(InstrumentationTestLanguage.EXPRESSION).build(), new ExecutionEventNodeFactory() {
            public ExecutionEventNode create(final EventContext context) {
                return new ExecutionEventNode() {
                    @CompilationFinal private boolean visited;

                    @Override
                    public void onReturnValue(VirtualFrame vFrame, Object result) {
                        if (!visited) {
                            CompilerDirectives.transferToInterpreterAndInvalidate();
                            visited = true;
                            out.print(context.getInstrumentedSourceSection().getCharIndex() + " ");
                            coverage.add(context.getInstrumentedSourceSection());
                        }
                    }

                };
            }
        });
    }

}
