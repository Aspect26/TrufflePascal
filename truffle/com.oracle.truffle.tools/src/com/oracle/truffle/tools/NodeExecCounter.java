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
package com.oracle.truffle.tools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrument.ASTProber;
import com.oracle.truffle.api.instrument.Instrumenter;
import com.oracle.truffle.api.instrument.Probe;
import com.oracle.truffle.api.instrument.ProbeException;
import com.oracle.truffle.api.instrument.ProbeFailure;
import com.oracle.truffle.api.instrument.ProbeInstrument;
import com.oracle.truffle.api.instrument.ProbeListener;
import com.oracle.truffle.api.instrument.StandardInstrumentListener;
import com.oracle.truffle.api.instrument.SyntaxTag;
import com.oracle.truffle.api.instrument.impl.DefaultProbeListener;
import com.oracle.truffle.api.instrument.impl.DefaultStandardInstrumentListener;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.Node.Child;
import com.oracle.truffle.api.nodes.NodeVisitor;
import com.oracle.truffle.api.nodes.RootNode;

/**
 * An {@linkplain Instrumenter.Tool Instrumentation Tool} that counts interpreter
 * <em>execution calls</em> to AST nodes, tabulated by the type of called nodes; counting can be
 * enabled <em>all</em> nodes or restricted to nodes with a specified {@linkplain SyntaxTag tag}
 * that is presumed to be applied external to the tool.
 * <p>
 * s <b>Tool Life Cycle</b>
 * <p>
 * See {@linkplain Instrumenter.Tool Instrumentation Tool} for the life cycle common to all such
 * tools.
 * </p>
 * <b>Execution Counts</b>
 * <p>
 * <ul>
 * <li>"Execution call" on a node is is defined as invocation of a node method that is instrumented
 * to produce the event {@link StandardInstrumentListener#onEnter(Probe, Node, VirtualFrame)};</li>
 * <li>Execution calls are tabulated only at <em>instrumented</em> nodes, i.e. those for which
 * {@linkplain Instrumenter#probe(Node) probing} is supported;</li>
 * <li>Execution calls are tabulated only at nodes present in the AST when originally created;
 * dynamically added nodes will not be instrumented.</li>
 * </ul>
 * </p>
 * <b>Failure Log</b>
 * <p>
 * For the benefit of language implementors, the tool maintains a log describing failed attempts to
 * probe AST nodes. Most failures occur when the type of the wrapper created by
 * {@link Node#createWrapperNode()} is not assignable to the relevant {@link Child} field in the
 * node's parent.
 * </p>
 * <p>
 * {@linkplain #reset() Resetting} the counts has no effect on the failure log.
 * </p>
 * <b>Results</b>
 * <p>
 * A modification-safe copy of the {@linkplain #getCounts() counts} can be retrieved at any time,
 * without effect on the state of the tool.
 * </p>
 * <p>
 * A "default" {@linkplain #print(PrintStream) print()} method can summarizes the current counts at
 * any time in a simple textual format, without effect on the state of the tool.
 * </p>
 *
 * @see ProbeInstrument
 * @see SyntaxTag
 * @see ProbeFailure
 */
@Deprecated
public final class NodeExecCounter extends Instrumenter.Tool {

    /**
     * Execution count for AST nodes of a particular type.
     */
    public interface NodeExecutionCount {
        Class<?> nodeClass();

        long executionCount();
    }

    /**
     * Listener for events at instrumented nodes. Counts are maintained in a shared table, so the
     * listener is stateless and can be shared by every {@link ProbeInstrument}.
     */
    private final StandardInstrumentListener instrumentListener = new DefaultStandardInstrumentListener() {
        @Override
        public void onEnter(Probe probe, Node node, VirtualFrame frame) {
            if (isEnabled()) {
                final Class<?> nodeClass = node.getClass();
                /*
                 * Everything up to here is inlined by Truffle compilation. Delegate the next part
                 * to a method behind an inlining boundary.
                 * 
                 * Note that it is not permitted to pass a {@link VirtualFrame} across an inlining
                 * boundary; they are truly virtual in inlined code.
                 */
                AtomicLong nodeCounter = getCounter(nodeClass);
                nodeCounter.getAndIncrement();
            }
        }

        /**
         * Mark this method as a boundary that will stop Truffle inlining, which should not be
         * allowed to inline the hash table method or any other complex library code.
         */
        @TruffleBoundary
        private AtomicLong getCounter(Class<?> nodeClass) {
            AtomicLong nodeCounter = counters.get(nodeClass);
            if (nodeCounter == null) {
                nodeCounter = new AtomicLong();
                counters.put(nodeClass, nodeCounter);
            }
            return nodeCounter;
        }
    };

    /** Counting data. */
    private final Map<Class<?>, AtomicLong> counters = new HashMap<>();

    /** Failure log. */
    private final List<ProbeFailure> failures = new ArrayList<>();

    /** For disposal. */
    private final List<ProbeInstrument> instruments = new ArrayList<>();

    /**
     * If non-null, counting is restricted to nodes holding this tag.
     */
    private final SyntaxTag countingTag;

    /**
     * Prober used only when instrumenting every node.
     */
    private ASTProber astProber;

    /**
     * Listener used only when restricting counting to a specific tag.
     */
    private ProbeListener probeListener;

    /**
     * Create a per node-type execution counting tool for all nodes in subsequently created ASTs.
     */
    public NodeExecCounter() {
        this(null);
    }

    /**
     * Creates a per-type execution counting for nodes tagged as specified in subsequently created
     * ASTs.
     */
    public NodeExecCounter(SyntaxTag tag) {
        this.countingTag = tag;
    }

    @Override
    protected boolean internalInstall() {
        if (countingTag == null) {
            astProber = new ExecCounterASTProber();
            getInstrumenter().registerASTProber(astProber);
        } else {
            probeListener = new NodeExecCounterProbeListener();
            getInstrumenter().addProbeListener(probeListener);
        }
        return true;
    }

    @Override
    protected void internalReset() {
        counters.clear();
        failures.clear();
    }

    @Override
    protected void internalDispose() {
        if (astProber != null) {
            getInstrumenter().unregisterASTProber(astProber);
        }
        if (probeListener != null) {
            getInstrumenter().removeProbeListener(probeListener);
        }
        for (ProbeInstrument instrument : instruments) {
            instrument.dispose();
        }
    }

    /**
     * Gets a modification-safe summary of the current per-type node execution counts; does not
     * affect the counts.
     */
    public NodeExecutionCount[] getCounts() {
        final Collection<Map.Entry<Class<?>, AtomicLong>> entrySet = counters.entrySet();
        final NodeExecutionCount[] result = new NodeExecCountImpl[entrySet.size()];
        int i = 0;
        for (Map.Entry<Class<?>, AtomicLong> entry : entrySet) {
            result[i++] = new NodeExecCountImpl(entry.getKey(), entry.getValue().longValue());
        }
        return result;
    }

    /**
     * Gets a log containing a report of every failed attempt to instrument a node.
     */
    public ProbeFailure[] getFailures() {
        return failures.toArray(new ProbeFailure[failures.size()]);
    }

    /**
     * A default printer for the current counts, producing lines of the form
     * " <count> : <node type>" in descending order of count.
     */
    public void print(PrintStream out) {
        print(out, false);
    }

    /**
     * A default printer for the current counts, producing lines of the form
     * " <count> : <node type>" in descending order of count.
     *
     * @param out
     * @param verbose whether to describe nodes on which instrumentation failed
     */
    public void print(PrintStream out, boolean verbose) {

        final long missedNodes = failures.size();
        out.println();
        if (countingTag == null) {
            out.println("Execution counts by node type:");
        } else {
            out.println("\"" + countingTag.name() + "\"-tagged execution counts by node type:");
        }
        final StringBuilder disclaim = new StringBuilder("(");
        if (missedNodes > 0) {
            disclaim.append(Long.toString(missedNodes) + " original AST nodes not instrumented, ");
        }
        disclaim.append("dynamically added nodes not instrumented)");
        out.println(disclaim.toString());
        NodeExecutionCount[] execCounts = getCounts();
        // Sort in descending order
        Arrays.sort(execCounts, new Comparator<NodeExecutionCount>() {

            public int compare(NodeExecutionCount o1, NodeExecutionCount o2) {
                return Long.compare(o2.executionCount(), o1.executionCount());
            }

        });
        for (NodeExecutionCount nodeCount : execCounts) {
            out.format("%12d", nodeCount.executionCount());
            out.println(" : " + nodeCount.nodeClass().getName());
        }

        if (verbose && missedNodes > 0) {
            out.println("Instrumentation failures for execution counts:");

            for (ProbeFailure failure : failures) {
                out.println("\t" + failure.getMessage());
            }
        }
    }

    /**
     * A prober that attempts to probe and instrument every node.
     */
    private class ExecCounterASTProber implements ASTProber {

        public void probeAST(final Instrumenter instrumenter, final RootNode startNode) {

            startNode.accept(new NodeVisitor() {

                public boolean visit(Node node) {
                    try {

                        final Probe probe = instrumenter.probe(node);
                        final ProbeInstrument instrument = instrumenter.attach(probe, instrumentListener, "NodeExecCounter");
                        instruments.add(instrument);
                    } catch (ProbeException ex) {
                        failures.add(ex.getFailure());
                    }
                    return true;
                }

            });
        }
    }

    /**
     * A listener that assumes ASTs have been tagged external to this tool, and which instruments
     * nodes holding a specified tag.
     */
    private class NodeExecCounterProbeListener extends DefaultProbeListener {

        @Override
        public void probeTaggedAs(Probe probe, SyntaxTag tag, Object tagValue) {
            if (countingTag == tag) {
                final ProbeInstrument instrument = getInstrumenter().attach(probe, instrumentListener, NodeExecCounter.class.getSimpleName());
                instruments.add(instrument);
            }
        }
    }

    private static class NodeExecCountImpl implements NodeExecutionCount {

        private final Class<?> nodeClass;
        private final long count;

        NodeExecCountImpl(Class<?> nodeClass, long count) {
            this.nodeClass = nodeClass;
            this.count = count;
        }

        @SuppressWarnings("deprecation")
        public Class<?> nodeClass() {
            return nodeClass;
        }

        @SuppressWarnings("deprecation")
        public long executionCount() {
            return count;
        }
    }
}
