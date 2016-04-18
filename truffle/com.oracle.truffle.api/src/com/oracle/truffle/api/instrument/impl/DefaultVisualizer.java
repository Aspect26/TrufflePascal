/*
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api.instrument.impl;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.instrument.ASTPrinter;
import com.oracle.truffle.api.instrument.Visualizer;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;

public class DefaultVisualizer implements Visualizer {

    private final ASTPrinter astPrinter;

    public DefaultVisualizer() {
        this.astPrinter = new DefaultASTPrinter();
    }

    @SuppressWarnings("deprecation")
    public ASTPrinter getASTPrinter() {
        return astPrinter;
    }

    @SuppressWarnings("deprecation")
    public String displaySourceLocation(Node node) {
        if (node == null) {
            return "<unknown>";
        }
        SourceSection section = node.getSourceSection();
        boolean estimated = false;
        if (section == null) {
            section = node.getEncapsulatingSourceSection();
            estimated = true;
        }
        if (section == null) {
            return "<error: source location>";
        }
        return section.getShortDescription() + (estimated ? "~" : "");
    }

    @SuppressWarnings("deprecation")
    public String displayMethodName(Node node) {
        if (node == null) {
            return null;
        }
        RootNode root = node.getRootNode();
        if (root == null) {
            return "unknown";
        }
        return root.getCallTarget().toString();
    }

    @SuppressWarnings("deprecation")
    public String displayCallTargetName(CallTarget callTarget) {
        return callTarget.toString();
    }

    @SuppressWarnings("deprecation")
    public String displayValue(Object value, int trim) {
        if (value == null) {
            return "<empty>";
        }
        return trim(value.toString(), trim);
    }

    @SuppressWarnings("deprecation")
    public String displayIdentifier(FrameSlot slot) {
        return slot.getIdentifier().toString();
    }

    /**
     * Trims text if {@code trim > 0} to the shorter of {@code trim} or the length of the first line
     * of test. Identity if {@code trim <= 0}.
     */
    protected String trim(String text, int trim) {
        if (trim == 0) {
            return text;
        }
        final String[] lines = text.split("\n");
        String result = lines[0];
        if (lines.length == 1) {
            if (result.length() <= trim) {
                return result;
            }
            if (trim <= 3) {
                return result.substring(0, Math.min(result.length() - 1, trim - 1));
            } else {
                return result.substring(0, trim - 4) + "...";
            }
        }
        return (result.length() < trim - 3 ? result : result.substring(0, trim - 4)) + "...";
    }
}
