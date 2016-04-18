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
package com.oracle.truffle.api.instrument;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeFieldAccessor;
import com.oracle.truffle.api.nodes.NodeUtil;

/**
 * Description of a failed attempt to instrument an AST node.
 * 
 * @since 0.8 or earlier
 */
public final class ProbeFailure {
    /** @since 0.8 or earlier */
    public enum Reason {

        /**
         * Node to be probed has no parent.
         * 
         * @since 0.8 or earlier
         */
        NO_PARENT("Node to be probed has no parent"),

        /**
         * The node to be probed is a wrapper.
         * 
         * @since 0.8 or earlier
         */
        WRAPPER_NODE("The node to be probed is a wrapper"),

        /**
         * The node to be probed does not support {@linkplain Instrumenter Instrumentation} .
         * 
         * @since 0.8 or earlier
         */
        NOT_INSTRUMENTABLE("The node to be project is \"not instrumentable\""),

        /**
         * No wrapper could be created that is also a {@link Node}.
         * 
         * @since 0.8 or earlier
         */
        NO_WRAPPER("No wrapper could be created"),

        /**
         * Wrapper not assignable to the parent's child field.
         * 
         * @since 0.8 or earlier
         */
        WRAPPER_TYPE("Wrapper not assignable to parent's child field");

        final String message;

        Reason(String message) {
            this.message = message;
        }

        /** @since 0.8 or earlier */
        public String getMessage() {
            return message;
        }
    }

    private final Reason reason;
    private final Node parent;
    private final Node child;
    private final Object wrapper;

    /**
     * Description of an internal failure of {@link Instrumenter#probe(Node)}.
     *
     * @param reason what caused the failure
     * @param parent the parent, if known, of the child being probed
     * @param child this child being probed
     * @param wrapper the {@link WrapperNode} created to implement the probe
     * @since 0.8 or earlier
     */
    public ProbeFailure(Reason reason, Node parent, Node child, Object wrapper) {
        this.reason = reason;
        this.parent = parent;
        this.child = child;
        this.wrapper = wrapper;
    }

    /**
     * @return a short explanation of the failure
     * @since 0.8 or earlier
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * @return the parent, if any, of the node being probed
     * @since 0.8 or earlier
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @return the node being probed
     * @since 0.8 or earlier
     */
    public Node getChild() {
        return child;
    }

    /**
     * @return the {@link WrapperNode} created for the probe attempt
     * @since 0.8 or earlier
     */
    public Object getWrapper() {
        return wrapper;
    }

    /** @since 0.8 or earlier */
    public String getMessage() {
        final StringBuilder sb = new StringBuilder(reason.message + ": ");
        if (parent != null) {
            sb.append("parent=" + parent.getClass().getSimpleName() + " ");
            if (child != null) {
                sb.append("child=" + child.getClass().getSimpleName() + " ");
                final NodeFieldAccessor field = NodeUtil.findChildField(parent, child);
                if (field != null) {
                    sb.append("field=" + field.getName() + " ");
                }
            }
        }
        if (wrapper != null) {
            sb.append("wrapper=" + wrapper.getClass().getSimpleName());
        }
        return sb.toString();
    }

}
