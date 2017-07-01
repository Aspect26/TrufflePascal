// CheckStyle: start generated
package com.oracle.truffle.sl.builtins;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.runtime.SLContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLDefineFunctionBuiltin.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class SLDefineFunctionBuiltinFactory implements NodeFactory<SLDefineFunctionBuiltin> {

    private static SLDefineFunctionBuiltinFactory instance;

    private SLDefineFunctionBuiltinFactory() {
    }

    @Override
    public Class<SLDefineFunctionBuiltin> getNodeClass() {
        return SLDefineFunctionBuiltin.class;
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
    public SLDefineFunctionBuiltin createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof SLExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof SLContext)) {
            return create((SLExpressionNode[]) arguments[0], (SLContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<SLDefineFunctionBuiltin> getInstance() {
        if (instance == null) {
            instance = new SLDefineFunctionBuiltinFactory();
        }
        return instance;
    }

    public static SLDefineFunctionBuiltin create(SLExpressionNode[] arguments, SLContext context) {
        return new SLDefineFunctionBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(SLDefineFunctionBuiltin.class)
    public static final class SLDefineFunctionBuiltinNodeGen extends SLDefineFunctionBuiltin {

        private final SLContext context;
        @Child private SLExpressionNode arguments0_;
        @CompilationFinal private int state_ = 1;

        private SLDefineFunctionBuiltinNodeGen(SLExpressionNode[] arguments, SLContext context) {
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
            if ((state & 0b10) != 0 /* is-active defineFunction(String) */ && arguments0Value_ instanceof String) {
                String arguments0Value__ = (String) arguments0Value_;
                return defineFunction(arguments0Value__);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            return executeAndSpecialize(arguments0Value_);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeGeneric(frameValue);
            return;
        }

        private String executeAndSpecialize(Object arguments0Value) {
            Lock lock = getLock();
            boolean hasLock = true;
            lock.lock();
            try {
                int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
                if (arguments0Value instanceof String) {
                    String arguments0Value_ = (String) arguments0Value;
                    this.state_ = state | 0b10 /* add-active defineFunction(String) */;
                    lock.unlock();
                    hasLock = false;
                    return defineFunction(arguments0Value_);
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
