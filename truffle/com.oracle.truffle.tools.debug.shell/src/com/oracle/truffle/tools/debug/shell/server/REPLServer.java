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
package com.oracle.truffle.tools.debug.shell.server;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.debug.Breakpoint;
import com.oracle.truffle.api.debug.Breakpoint.State;
import com.oracle.truffle.api.debug.Debugger;
import com.oracle.truffle.api.debug.ExecutionEvent;
import com.oracle.truffle.api.debug.SuspendedEvent;
import com.oracle.truffle.api.frame.FrameInstance;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.instrument.StandardSyntaxTag;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.LineLocation;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.api.vm.EventConsumer;
import com.oracle.truffle.api.vm.PolyglotEngine;
import com.oracle.truffle.api.vm.PolyglotEngine.Language;
import com.oracle.truffle.api.vm.PolyglotEngine.Value;
import com.oracle.truffle.tools.debug.shell.REPLMessage;
import com.oracle.truffle.tools.debug.shell.client.SimpleREPLClient;
import com.oracle.truffle.tools.debug.shell.server.InstrumentationUtils.ASTPrinter;
import com.oracle.truffle.tools.debug.shell.server.InstrumentationUtils.LocationPrinter;

/**
 * The server side of a simple message-based protocol for a possibly remote language
 * Read-Eval-Print-Loop.
 */
public final class REPLServer {

    private static final boolean TRACE = Boolean.getBoolean("truffle.debug.trace");
    private static final String TRACE_PREFIX = "REPLSrv: ";
    private static final PrintStream OUT = System.out;

    private static void trace(String format, Object... args) {
        if (TRACE) {
            OUT.println(TRACE_PREFIX + String.format(format, args));
        }
    }

    private static String describeObject(Object obj) {
        if (obj == null) {
            return "null";
        }
        String name = obj.toString();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    private static int nextBreakpointUID = 0;

    // Language-agnostic
    private final PolyglotEngine engine;
    private Debugger db;
    private Context currentServerContext;
    private SimpleREPLClient replClient = null;
    private String statusPrefix;
    private final Map<String, REPLHandler> handlerMap = new HashMap<>();
    private ASTPrinter astPrinter = new InstrumentationUtils.ASTPrinter();
    private LocationPrinter locationPrinter = new InstrumentationUtils.LocationPrinter();
    private Visualizer visualizer = new Visualizer();

    /** Languages sorted by name. */
    private final TreeSet<Language> engineLanguages = new TreeSet<>(new Comparator<Language>() {

        public int compare(Language lang1, Language lang2) {
            return lang1.getName().compareTo(lang2.getName());
        }
    });

    /** MAP: language name => Language. */
    private final Map<String, Language> nameToLanguage = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    // TODO (mlvdv) Language-specific
    private PolyglotEngine.Language defaultLanguage;

    private Map<Integer, BreakpointInfo> breakpoints = new WeakHashMap<>();

    public REPLServer(String defaultMIMEType) {
        this.engine = PolyglotEngine.newBuilder().onEvent(onHalted).onEvent(onExec).build();
        this.db = Debugger.find(this.engine);
        engineLanguages.addAll(engine.getLanguages().values());
        if (engineLanguages.size() == 0) {
            throw new RuntimeException("No language implementations installed");
        }
        for (Language language : engineLanguages) {
            nameToLanguage.put(language.getName(), language);
        }

        if (defaultMIMEType == null) {
            defaultLanguage = engineLanguages.iterator().next();
        } else {
            this.defaultLanguage = engine.getLanguages().get(defaultMIMEType);
            if (defaultLanguage == null) {
                throw new RuntimeException("Implementation not found for \"" + defaultMIMEType + "\"");
            }
        }
        statusPrefix = languageName(defaultLanguage);
    }

    private final EventConsumer<SuspendedEvent> onHalted = new EventConsumer<SuspendedEvent>(SuspendedEvent.class) {
        @Override
        protected void on(SuspendedEvent ev) {
            if (TRACE) {
                trace(" on %s", describeObject(ev));
            }
            REPLServer.this.haltedAt(ev);
            if (TRACE) {
                trace("END on %s", describeObject(ev));
            }
        }
    };

    private final EventConsumer<ExecutionEvent> onExec = new EventConsumer<ExecutionEvent>(ExecutionEvent.class) {
        @Override
        protected void on(ExecutionEvent event) {
            if (TRACE) {
                trace("BEGIN on %s debugger=%s", describeObject(event), describeObject(db));
            }
            if (currentServerContext.steppingInto) {
                event.prepareStepInto();
            }
            if (TRACE) {
                trace("END on %s debugger=%s", describeObject(event), describeObject(db));
            }
        }
    };

    public void add(REPLHandler handler) {
        handlerMap.put(handler.getOp(), handler);
    }

    /**
     * Starts up a server; status returned in a message.
     */
    public void start() {

        add(REPLHandler.BACKTRACE_HANDLER);
        add(REPLHandler.BREAK_AT_LINE_HANDLER);
        add(REPLHandler.BREAK_AT_LINE_ONCE_HANDLER);
        add(REPLHandler.BREAKPOINT_INFO_HANDLER);
        add(REPLHandler.CALL_HANDLER);
        add(REPLHandler.CLEAR_BREAK_HANDLER);
        add(REPLHandler.CONTINUE_HANDLER);
        add(REPLHandler.DELETE_HANDLER);
        add(REPLHandler.DISABLE_BREAK_HANDLER);
        add(REPLHandler.ENABLE_BREAK_HANDLER);
        add(REPLHandler.EVAL_HANDLER);
        add(REPLHandler.FILE_HANDLER);
        add(REPLHandler.FRAME_HANDLER);
        add(REPLHandler.INFO_HANDLER);
        add(REPLHandler.KILL_HANDLER);
        add(REPLHandler.LOAD_HANDLER);
        add(REPLHandler.QUIT_HANDLER);
        add(REPLHandler.SET_BREAK_CONDITION_HANDLER);
        add(REPLHandler.SET_LANGUAGE_HANDLER);
        add(REPLHandler.STEP_INTO_HANDLER);
        add(REPLHandler.STEP_OUT_HANDLER);
        add(REPLHandler.STEP_OVER_HANDLER);
        add(REPLHandler.TRUFFLE_HANDLER);
        add(REPLHandler.TRUFFLE_NODE_HANDLER);
        add(REPLHandler.UNSET_BREAK_CONDITION_HANDLER);
        this.replClient = new SimpleREPLClient(this);
        this.currentServerContext = new Context(null, null, defaultLanguage);
        replClient.start();
    }

    @SuppressWarnings("static-method")
    public String getWelcome() {
        return "GraalVM MultiLanguage Debugger 0.9\n" + "Copyright (c) 2013-5, Oracle and/or its affiliates";
    }

    public ASTPrinter getASTPrinter() {
        return astPrinter;
    }

    public LocationPrinter getLocationPrinter() {
        return locationPrinter;
    }

    void haltedAt(SuspendedEvent event) {
        // Message the client that execution is halted and is in a new debugging context
        final REPLMessage message = new REPLMessage();
        message.put(REPLMessage.OP, REPLMessage.STOPPED);

        // Identify language execution where halted; default to previous context
        Language haltedLanguage = currentServerContext.currentLanguage;
        final String mimeType = findMime(event.getNode());
        if (mimeType == null) {
            message.put(REPLMessage.WARNINGS, "unable to detect language at halt");
        } else {
            final Language language = engine.getLanguages().get(mimeType);
            if (language == null) {
                message.put(REPLMessage.WARNINGS, "no language installed for MIME type \"" + mimeType + "\"");
            } else {
                haltedLanguage = language;
            }
        }

        // Create and push a new debug context where execution is halted
        currentServerContext = new Context(currentServerContext, event, haltedLanguage);

        message.put(REPLMessage.LANG_NAME, haltedLanguage.getName());
        final SourceSection src = event.getNode().getSourceSection();
        final Source source = src.getSource();
        message.put(REPLMessage.SOURCE_NAME, source.getName());
        final String path = source.getPath();
        if (path == null) {
            message.put(REPLMessage.SOURCE_TEXT, source.getCode());
        } else {
            message.put(REPLMessage.FILE_PATH, path);
        }
        message.put(REPLMessage.LINE_NUMBER, Integer.toString(src.getStartLine()));

        message.put(REPLMessage.STATUS, REPLMessage.SUCCEEDED);
        message.put(REPLMessage.DEBUG_LEVEL, Integer.toString(currentServerContext.getLevel()));
        List<String> warnings = event.getRecentWarnings();
        if (!warnings.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            for (String warning : warnings) {
                sb.append(warning + "\n");
            }
            message.put(REPLMessage.WARNINGS, sb.toString());
        }
        try {
            // Cheat with synchrony: call client directly about entering a nested debugging
            // context.
            replClient.halted(message);
        } finally {
            // Returns when "continue" or "kill" is called in the new debugging context

            // Pop the debug context, and return so that the old context will continue
            currentServerContext = currentServerContext.predecessor;
        }
    }

    @SuppressWarnings("static-method")
    private String findMime(Node node) {
        String result = null;
        final SourceSection section = node.getEncapsulatingSourceSection();
        if (section != null) {
            final Source source = section.getSource();
            if (source != null) {
                result = source.getMimeType();
            }
        }
        return result;
    }

    /**
     * Execution context of a halted program, possibly nested.
     */
    public final class Context {

        private final Context predecessor;
        private final int level;
        private final SuspendedEvent event;
        private Language currentLanguage;
        private boolean steppingInto = false;  // Only true during a "stepInto" engine call

        Context(Context predecessor, SuspendedEvent event, Language language) {
            assert language != null;
            this.level = predecessor == null ? 0 : predecessor.getLevel() + 1;
            this.predecessor = predecessor;
            this.event = event;
            this.currentLanguage = language;
        }

        /**
         * The nesting depth of this context in the current session.
         */
        int getLevel() {
            return level;
        }

        /**
         * The AST node where execution is halted in this context.
         */
        Node getNodeAtHalt() {
            return event.getNode();
        }

        /**
         * Get access to display methods appropriate to the language at halted node.
         */
        Visualizer getVisualizer() {
            return visualizer;
        }

        Object call(String name, boolean stepInto, List<String> argList) throws IOException {
            Value symbol = engine.findGlobalSymbol(name);
            if (symbol == null) {
                throw new IOException("symbol \"" + name + "\" not found");
            }
            final List<Object> args = new ArrayList<>();
            for (String stringArg : argList) {
                Integer intArg = null;
                try {
                    intArg = Integer.valueOf(stringArg);
                    args.add(intArg);
                } catch (NumberFormatException e) {
                    args.add(stringArg);
                }
            }
            this.steppingInto = stepInto;
            try {
                return symbol.execute(args.toArray(new Object[0])).get();
            } finally {
                this.steppingInto = false;
            }
        }

        void eval(Source source, boolean stepInto) throws IOException {
            this.steppingInto = stepInto;
            try {
                engine.eval(source);
            } finally {
                this.steppingInto = false;
            }
        }

        /**
         * Evaluates a code snippet in the context of a selected frame in the currently suspended
         * execution, if any; otherwise a top level (new) evaluation.
         *
         * @param code the snippet to evaluate
         * @param frameNumber index of the stack frame in which to evaluate, 0 = current frame where
         *            halted, null = top level eval
         * @return result of the evaluation
         * @throws IOException if something goes wrong
         */
        Object eval(String code, Integer frameNumber, boolean stepInto) throws IOException {
            if (event == null) {
                if (frameNumber != null) {
                    throw new IllegalStateException("Frame number requires a halted execution");
                }
                this.steppingInto = stepInto;
                final String mimeType = defaultMIME(currentLanguage);
                try {
                    return engine.eval(Source.fromText(code, "eval(\"" + code + "\")").withMimeType(mimeType)).get();
                } finally {
                    this.steppingInto = false;
                }
            } else {
                if (frameNumber == null) {
                    throw new IllegalStateException("Eval in halted context requires a frame number");
                }
                if (stepInto) {
                    event.prepareStepInto(1);
                }
                try {
                    FrameInstance frame = frameNumber == 0 ? null : event.getStack().get(frameNumber - 1);
                    final Object result = event.eval(code, frame);
                    return (result instanceof Value) ? ((Value) result).get() : result;
                } finally {
                    event.prepareContinue();
                }
            }
        }

        /**
         * The frame where execution is halted in this context.
         */
        MaterializedFrame getFrameAtHalt() {
            return event.getFrame();
        }

        /**
         * Dispatches a REPL request to the appropriate handler.
         */
        REPLMessage[] receive(REPLMessage request) {
            final String command = request.get(REPLMessage.OP);
            final REPLHandler handler = handlerMap.get(command);

            if (handler == null) {
                final REPLMessage message = new REPLMessage();
                message.put(REPLMessage.OP, command);
                message.put(REPLMessage.STATUS, REPLMessage.FAILED);
                message.put(REPLMessage.DISPLAY_MSG, statusPrefix + " op \"" + command + "\" not supported");
                final REPLMessage[] reply = new REPLMessage[]{message};
                return reply;
            }
            return handler.receive(request, REPLServer.this);
        }

        /**
         * @return Node where halted
         */
        Node getNode() {
            return event.getNode();
        }

        /**
         * @return Frame where halted
         */
        MaterializedFrame getFrame() {
            return event.getFrame();
        }

        /**
         * Provides access to the execution stack, not counting the node/frame where halted.
         *
         * @return immutable list of stack elements
         */
        List<FrameInstance> getStack() {
            return event.getStack();
        }

        public String getLanguageName() {
            return currentLanguage.getName();
        }

        /**
         * Case-insensitive; returns actual language name set.
         *
         * @throws IOException if fails
         */
        String setLanguage(String name) throws IOException {
            assert name != null;
            final Language language = nameToLanguage.get(name);
            if (language == null) {
                throw new IOException("Language \" + name + \" not supported");
            }
            if (language == currentLanguage) {
                return currentLanguage.getName();
            }
            if (event != null) {
                throw new IOException("Only supported at top level");
            }
            this.currentLanguage = language;
            return language.getName();
        }

        void prepareStepOut() {
            event.prepareStepOut();
        }

        void prepareStepInto(int repeat) {
            event.prepareStepInto(repeat);
        }

        void prepareStepOver(int repeat) {
            event.prepareStepOver(repeat);
        }

        void prepareContinue() {
            event.prepareContinue();
        }

    }

    /**
     * Ask the server to handle a request. Return a non-empty array of messages to simulate remote
     * operation where the protocol has possibly multiple messages being returned asynchronously in
     * response to each request.
     */
    public REPLMessage[] receive(REPLMessage request) {
        if (currentServerContext == null) {
            final REPLMessage message = new REPLMessage();
            message.put(REPLMessage.STATUS, REPLMessage.FAILED);
            message.put(REPLMessage.DISPLAY_MSG, "server not started");
            final REPLMessage[] reply = new REPLMessage[]{message};
            return reply;
        }
        return currentServerContext.receive(request);
    }

    Context getCurrentContext() {
        return currentServerContext;
    }

    // TODO (mlvdv) language-specific
    Language getLanguage() {
        return defaultLanguage;
    }

    TreeSet<Language> getLanguages() {
        return engineLanguages;
    }

    // TODO (mlvdv) language-specific
    public String getLanguageName() {
        return languageName(this.defaultLanguage);
    }

    private static String languageName(Language lang) {
        return lang.getName() + "(" + lang.getVersion() + ")";
    }

    @SuppressWarnings("static-method")
    private String defaultMIME(Language language) {
        return language.getMimeTypes().iterator().next();
    }

    BreakpointInfo setLineBreakpoint(int ignoreCount, LineLocation lineLocation, boolean oneShot) throws IOException {
        final BreakpointInfo info = new LineBreakpointInfo(lineLocation, ignoreCount, oneShot);
        info.activate();
        return info;
    }

    @Deprecated
    BreakpointInfo setTagBreakpoint(int ignoreCount, StandardSyntaxTag tag, boolean oneShot) throws IOException {
        final BreakpointInfo info = new TagBreakpointInfo(tag, ignoreCount, oneShot);
        info.activate();
        return info;
    }

    synchronized BreakpointInfo findBreakpoint(int id) {
        return breakpoints.get(id);
    }

    /**
     * Gets a list of the currently existing breakpoints.
     */
    Collection<BreakpointInfo> getBreakpoints() {
        // TODO (mlvdv) check if each is currently resolved
        return new ArrayList<>(breakpoints.values());
    }

    final class LineBreakpointInfo extends BreakpointInfo {

        private final LineLocation lineLocation;

        private LineBreakpointInfo(LineLocation lineLocation, int ignoreCount, boolean oneShot) {
            super(ignoreCount, oneShot);
            this.lineLocation = lineLocation;
        }

        @Override
        protected void activate() throws IOException {
            breakpoint = db.setLineBreakpoint(ignoreCount, lineLocation, oneShot);
            // TODO (mlvdv) check if resolved
            breakpoints.put(uid, this);
        }

        @Override
        String describeLocation() {
            if (breakpoint == null) {
                return "Line: " + lineLocation.getShortDescription();
            }
            return breakpoint.getLocationDescription();
        }

    }

    final class TagBreakpointInfo extends BreakpointInfo {
        private final StandardSyntaxTag tag;

        private TagBreakpointInfo(StandardSyntaxTag tag, int ignoreCount, boolean oneShot) {
            super(ignoreCount, oneShot);
            this.tag = tag;
        }

        @Override
        @SuppressWarnings("deprecation")
        protected void activate() throws IOException {
            breakpoint = db.setTagBreakpoint(ignoreCount, tag, oneShot);
            // TODO (mlvdv) check if resolved
            breakpoints.put(uid, this);

        }

        @Override
        String describeLocation() {
            if (breakpoint == null) {
                return "Tag: " + tag;
            }
            return breakpoint.getLocationDescription();
        }
    }

    abstract class BreakpointInfo {

        protected final int uid;
        protected final boolean oneShot;
        protected final int ignoreCount;

        protected State state = State.ENABLED_UNRESOLVED;
        protected Breakpoint breakpoint;
        protected Source conditionSource;

        protected BreakpointInfo(int ignoreCount, boolean oneShot) {
            this.ignoreCount = ignoreCount;
            this.oneShot = oneShot;
            this.uid = nextBreakpointUID++;
        }

        protected abstract void activate() throws IOException;

        abstract String describeLocation();

        int getID() {
            return uid;
        }

        String describeState() {
            return (breakpoint == null ? state : breakpoint.getState()).getName();
        }

        void setEnabled(boolean enabled) {
            if (breakpoint == null) {
                switch (state) {
                    case ENABLED_UNRESOLVED:
                        if (!enabled) {
                            state = State.DISABLED_UNRESOLVED;
                        }
                        break;
                    case DISABLED_UNRESOLVED:
                        if (enabled) {
                            state = State.ENABLED_UNRESOLVED;
                        }
                        break;
                    case DISPOSED:
                        throw new IllegalStateException("Disposed breakpoints must stay disposed");
                    default:
                        throw new IllegalStateException("Unexpected breakpoint state");
                }
            } else {
                breakpoint.setEnabled(enabled);
            }
        }

        boolean isEnabled() {
            return breakpoint == null ? (state == State.ENABLED_UNRESOLVED) : breakpoint.isEnabled();
        }

        void setCondition(String expr) throws IOException {
            if (breakpoint == null) {
                conditionSource = expr == null ? null : Source.fromText(expr, "breakpoint condition from text: " + expr);
            } else {
                breakpoint.setCondition(expr);
            }
        }

        String getCondition() {
            final Source source = breakpoint == null ? conditionSource : breakpoint.getCondition();
            return source == null ? null : source.getCode();
        }

        int getIgnoreCount() {
            return breakpoint == null ? ignoreCount : breakpoint.getIgnoreCount();
        }

        int getHitCount() {
            return breakpoint == null ? 0 : breakpoint.getHitCount();
        }

        void dispose() {
            if (breakpoint == null) {
                if (state == State.DISPOSED) {
                    throw new IllegalStateException("Breakpoint already disposed");
                }
            } else {
                breakpoint.dispose();
                breakpoint = null;
            }
            state = State.DISPOSED;
            breakpoints.remove(uid);
            conditionSource = null;
        }

        String summarize() {
            final StringBuilder sb = new StringBuilder("Breakpoint");
            sb.append(" id=" + uid);
            sb.append(" locn=(" + describeLocation());
            sb.append(") " + describeState());
            return sb.toString();
        }
    }

    static class Visualizer {

        /**
         * A short description of a source location in terms of source + line number.
         */
        String displaySourceLocation(Node node) {
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

        /**
         * Describes the name of the method containing a node.
         */
        String displayMethodName(Node node) {
            if (node == null) {
                return null;
            }
            RootNode root = node.getRootNode();
            if (root == null) {
                return "unknown";
            }
            return root.getCallTarget().toString();
        }

        /**
         * The name of the method.
         */
        String displayCallTargetName(CallTarget callTarget) {
            return callTarget.toString();
        }

        /**
         * Converts a value in the guest language to a display string. If
         *
         * @param trim if {@code > 0}, them limit size of String to either the value of trim or the
         *            number of characters in the first line, whichever is lower.
         */
        String displayValue(Object value, int trim) {
            if (value == null) {
                return "<empty>";
            }
            return trim(value.toString(), trim);
        }

        /**
         * Converts a slot identifier in the guest language to a display string.
         */
        String displayIdentifier(FrameSlot slot) {
            return slot.getIdentifier().toString();
        }

        /**
         * Trims text if {@code trim > 0} to the shorter of {@code trim} or the length of the first
         * line of test. Identity if {@code trim <= 0}.
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
}
