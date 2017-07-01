// CheckStyle: start generated
package com.oracle.truffle.sl.builtins;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.runtime.SLContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLIsNullBuiltin.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class SLIsNullBuiltinFactory implements NodeFactory<SLIsNullBuiltin> {

    private static SLIsNullBuiltinFactory instance;

    private SLIsNullBuiltinFactory() {
    }

    @Override
    public Class<SLIsNullBuiltin> getNodeClass() {
        return SLIsNullBuiltin.class;
    }

    @Override
    public List getExecutionSignature() {
        return Arrays.asList(SLExpressionNode.class);
    }

    @Override
    public List getNodeSignatures() {
        return Arrays.asList(Arrays.asList(SLExpressionNode[].class, SLContext.class));
    }

    @Override
    public SLIsNullBuiltin createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof SLExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof SLContext)) {
            return create((SLExpressionNode[]) arguments[0], (SLContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<SLIsNullBuiltin> getInstance() {
        if (instance == null) {
            instance = new SLIsNullBuiltinFactory();
        }
        return instance;
    }

    public static SLIsNullBuiltin create(SLExpressionNode[] arguments, SLContext context) {
        return new SLIsNullBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(SLIsNullBuiltin.class)
    public static final class SLIsNullBuiltinNodeGen extends SLIsNullBuiltin {

        private final SLContext context;
        @Child private SLExpressionNode arguments0_;
        @CompilationFinal private int state_ = 1;

        private SLIsNullBuiltinNodeGen(SLExpressionNode[] arguments, SLContext context) {
            this.context = context;
            this.arguments0_ = arguments != null && 0 < arguments.length ? arguments[0] : null;
        }

        @Override
        public SLContext getContext() {
            return this.context;
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            int state = state_;
            Object arguments0Value_ = arguments0_.executeGeneric(frameValue);
            if ((state & 0b10) != 0 /* is-active isNull(VirtualFrame, TruffleObject) */ && arguments0Value_ instanceof TruffleObject) {
                TruffleObject arguments0Value__ = (TruffleObject) arguments0Value_;
                return isNull(frameValue, arguments0Value__);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            return executeAndSpecialize(frameValue, arguments0Value_);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeGeneric(frameValue);
            return;
        }

        private Object executeAndSpecialize(VirtualFrame frameValue, Object arguments0Value) {
            Lock lock = getLock();
            boolean hasLock = true;
            lock.lock();
            try {
                int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
                if (arguments0Value instanceof TruffleObject) {
                    TruffleObject arguments0Value_ = (TruffleObject) arguments0Value;
                    this.state_ = state | 0b10 /* add-active isNull(VirtualFrame, TruffleObject) */;
                    lock.unlock();
                    hasLock = false;
                    return isNull(frameValue, arguments0Value_);
                }
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new UnsupportedSpecializationException(this, new Node[] {this.arguments0_}, arguments0Value);
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
