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

@GeneratedBy(SLNanoTimeBuiltin.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class SLNanoTimeBuiltinFactory implements NodeFactory<SLNanoTimeBuiltin> {

    private static SLNanoTimeBuiltinFactory instance;

    private SLNanoTimeBuiltinFactory() {
    }

    @Override
    public Class<SLNanoTimeBuiltin> getNodeClass() {
        return SLNanoTimeBuiltin.class;
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
    public SLNanoTimeBuiltin createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof SLExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof SLContext)) {
            return create((SLExpressionNode[]) arguments[0], (SLContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<SLNanoTimeBuiltin> getInstance() {
        if (instance == null) {
            instance = new SLNanoTimeBuiltinFactory();
        }
        return instance;
    }

    public static SLNanoTimeBuiltin create(SLExpressionNode[] arguments, SLContext context) {
        return new SLNanoTimeBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(SLNanoTimeBuiltin.class)
    public static final class SLNanoTimeBuiltinNodeGen extends SLNanoTimeBuiltin {

        private final SLContext context;
        @CompilationFinal private int state_ = 1;

        @SuppressWarnings("unused")
        private SLNanoTimeBuiltinNodeGen(SLExpressionNode[] arguments, SLContext context) {
            this.context = context;
        }

        @Override
        public SLContext getContext() {
            return this.context;
        }

        @Override
        public long executeLong(VirtualFrame frameValue) {
            int state = state_;
            if ((state & 0b10) != 0 /* is-active nanoTime() */) {
                return nanoTime();
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            return executeAndSpecialize();
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            return executeLong(frameValue);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeLong(frameValue);
            return;
        }

        private long executeAndSpecialize() {
            Lock lock = getLock();
            boolean hasLock = true;
            lock.lock();
            try {
                int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
                this.state_ = state | 0b10 /* add-active nanoTime() */;
                lock.unlock();
                hasLock = false;
                return nanoTime();
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
