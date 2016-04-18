/*
 * Copyright (c) 2013, 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.api.instrument;

import com.oracle.truffle.api.nodes.Node;
import java.io.PrintWriter;

/**
 * Access to AST-based debugging support, which is could be language implementation specific in the
 * details chosen to be presented.
 * <p>
 * <strong>WARNING:</strong> this interface is under development and will change substantially.
 * 
 * @since 0.8 or earlier
 */
public interface ASTPrinter {

    /**
     * Prints a textual AST display, one line per node, with nesting.
     *
     * @param p
     * @param node the root node of the display.
     * @param maxDepth the maximum number of levels to print below the root
     * @param markNode a node to mark with a textual arrow prefix, if present.
     * @since 0.8 or earlier
     */
    void printTree(PrintWriter p, Node node, int maxDepth, Node markNode);

    /**
     * Creates a textual AST display, one line per node, with nesting.
     *
     * @param node the root node of the display.
     * @param maxDepth the maximum number of levels to print below the root
     * @param markNode a node to mark with a textual arrow prefix, if present.
     * @since 0.8 or earlier
     */
    String printTreeToString(Node node, int maxDepth, Node markNode);

    /**
     * Creates a textual AST display, one line per node, with nesting.
     *
     * @param node the root node of the display.
     * @param maxDepth the maximum number of levels to print below the root
     * @since 0.8 or earlier
     */
    String printTreeToString(Node node, int maxDepth);

    /**
     * Creates a textual display describing a single (non-wrapper) node, including instrumentation
     * status: if Probed, and any tags.
     * 
     * @since 0.8 or earlier
     */
    String printNodeWithInstrumentation(Node node);
}
