// CheckStyle: start generated
package com.oracle.truffle.sl.builtins;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.runtime.SLContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLStackTraceBuiltin.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class SLStackTraceBuiltinFactory implements NodeFactory<SLStackTraceBuiltin> {

    private static SLStackTraceBuiltinFactory instance;

    private SLStackTraceBuiltinFactory() {
    }

    @Override
    public Class<SLStackTraceBuiltin> getNodeClass() {
        return SLStackTraceBuiltin.class;
    }

    @Override
    public List getExecutionSignature() {
        return Arrays.asList();
    }

    @Override
    public List getNodeSignatures() {
        return Arrays.asList(Arrays.asList(SLExpressionNode[].class, SLContext.class));
    }

    @Override
    public SLStackTraceBuiltin createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof SLExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof SLContext)) {
            return create((SLExpressionNode[]) arguments[0], (SLContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<SLStackTraceBuiltin> getInstance() {
        if (instance == null) {
            instance = new SLStackTraceBuiltinFactory();
        }
        return instance;
    }

    public static SLStackTraceBuiltin create(SLExpressionNode[] arguments, SLContext context) {
        return new SLStackTraceBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(SLStackTraceBuiltin.class)
    public static final class SLStackTraceBuiltinNodeGen extends SLStackTraceBuiltin {

        private final SLContext context;
        @CompilationFinal private int state_ = 1;

        @SuppressWarnings("unused")
        private SLStackTraceBuiltinNodeGen(SLExpressionNode[] arguments, SLContext context) {
            this.context = context;
        }

        @Override
        public SLContext getContext() {
            return this.context;
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            int state = state_;
            if ((state & 0b10) != 0 /* is-active trace() */) {
                return trace();
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            return executeAndSpecialize();
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeGeneric(frameValue);
            return;
        }

        private String executeAndSpecialize() {
            Lock lock = getLock();
            boolean hasLock = true;
            lock.lock();
            try {
                int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
                this.state_ = state | 0b10 /* add-active trace() */;
                lock.unlock();
                hasLock = false;
                return trace();
            } finally {
                if (hasLock) {
                    lock.unlock();
                }
            }
        }

        @Override
        public NodeCost getCost() {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            if (state == 0b0) {
                return NodeCost.UNINITIALIZED;
            } else {
                return NodeCost.MONOMORPHIC;
            }
        }

    }
}
