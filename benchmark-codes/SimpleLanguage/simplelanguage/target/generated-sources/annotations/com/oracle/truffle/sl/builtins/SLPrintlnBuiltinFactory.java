// CheckStyle: start generated
package com.oracle.truffle.sl.builtins;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import com.oracle.truffle.sl.runtime.SLContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLPrintlnBuiltin.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class SLPrintlnBuiltinFactory implements NodeFactory<SLPrintlnBuiltin> {

    private static SLPrintlnBuiltinFactory instance;

    private SLPrintlnBuiltinFactory() {
    }

    @Override
    public Class<SLPrintlnBuiltin> getNodeClass() {
        return SLPrintlnBuiltin.class;
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
    public SLPrintlnBuiltin createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof SLExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof SLContext)) {
            return create((SLExpressionNode[]) arguments[0], (SLContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<SLPrintlnBuiltin> getInstance() {
        if (instance == null) {
            instance = new SLPrintlnBuiltinFactory();
        }
        return instance;
    }

    public static SLPrintlnBuiltin create(SLExpressionNode[] arguments, SLContext context) {
        return new SLPrintlnBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(SLPrintlnBuiltin.class)
    public static final class SLPrintlnBuiltinNodeGen extends SLPrintlnBuiltin {

        private final SLContext context;
        @Child private SLExpressionNode arguments0_;
        @CompilationFinal private int state_ = 1;

        private SLPrintlnBuiltinNodeGen(SLExpressionNode[] arguments, SLContext context) {
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
            if ((state & 0b11101) == 0 /* only-active println(long) */) {
                return executeGeneric_long0(frameValue, state);
            } else if ((state & 0b11011) == 0 /* only-active println(boolean) */) {
                return executeGeneric_boolean1(frameValue, state);
            } else {
                return executeGeneric_generic2(frameValue, state);
            }
        }

        private Object executeGeneric_long0(VirtualFrame frameValue, int state) {
            long arguments0Value_;
            try {
                arguments0Value_ = arguments0_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                return executeAndSpecialize(ex.getResult());
            }
            assert (state & 0b10) != 0 /* is-active println(long) */;
            return println(arguments0Value_);
        }

        private Object executeGeneric_boolean1(VirtualFrame frameValue, int state) {
            boolean arguments0Value_;
            try {
                arguments0Value_ = arguments0_.executeBoolean(frameValue);
            } catch (UnexpectedResultException ex) {
                return executeAndSpecialize(ex.getResult());
            }
            assert (state & 0b100) != 0 /* is-active println(boolean) */;
            return println(arguments0Value_);
        }

        private Object executeGeneric_generic2(VirtualFrame frameValue, int state) {
            Object arguments0Value_ = arguments0_.executeGeneric(frameValue);
            if ((state & 0b10) != 0 /* is-active println(long) */ && arguments0Value_ instanceof Long) {
                long arguments0Value__ = (long) arguments0Value_;
                return println(arguments0Value__);
            }
            if ((state & 0b100) != 0 /* is-active println(boolean) */ && arguments0Value_ instanceof Boolean) {
                boolean arguments0Value__ = (boolean) arguments0Value_;
                return println(arguments0Value__);
            }
            if ((state & 0b1000) != 0 /* is-active println(String) */ && arguments0Value_ instanceof String) {
                String arguments0Value__ = (String) arguments0Value_;
                return println(arguments0Value__);
            }
            if ((state & 0b10000) != 0 /* is-active println(Object) */) {
                return println(arguments0Value_);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            return executeAndSpecialize(arguments0Value_);
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
            int state = state_;
            if ((state & 0b10000) != 0 /* is-active println(Object) */) {
                return SLTypesGen.expectBoolean(executeGeneric(frameValue));
            }
            boolean arguments0Value_;
            try {
                arguments0Value_ = arguments0_.executeBoolean(frameValue);
            } catch (UnexpectedResultException ex) {
                return SLTypesGen.expectBoolean(executeAndSpecialize(ex.getResult()));
            }
            if ((state & 0b100) != 0 /* is-active println(boolean) */) {
                return println(arguments0Value_);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            return SLTypesGen.expectBoolean(executeAndSpecialize(arguments0Value_));
        }

        @Override
        public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
            int state = state_;
            if ((state & 0b10000) != 0 /* is-active println(Object) */) {
                return SLTypesGen.expectLong(executeGeneric(frameValue));
            }
            long arguments0Value_;
            try {
                arguments0Value_ = arguments0_.executeLong(frameValue);
            } catch (UnexpectedResultException ex) {
                return SLTypesGen.expectLong(executeAndSpecialize(ex.getResult()));
            }
            if ((state & 0b10) != 0 /* is-active println(long) */) {
                return println(arguments0Value_);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            return SLTypesGen.expectLong(executeAndSpecialize(arguments0Value_));
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            int state = state_;
            try {
                if ((state & 0b11101) == 0 /* only-active println(long) */) {
                    executeLong(frameValue);
                    return;
                } else if ((state & 0b11011) == 0 /* only-active println(boolean) */) {
                    executeBoolean(frameValue);
                    return;
                }
                executeGeneric(frameValue);
                return;
            } catch (UnexpectedResultException ex) {
                return;
            }
        }

        private Object executeAndSpecialize(Object arguments0Value) {
            Lock lock = getLock();
            boolean hasLock = true;
            lock.lock();
            try {
                int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
                if (arguments0Value instanceof Long) {
                    long arguments0Value_ = (long) arguments0Value;
                    this.state_ = state | 0b10 /* add-active println(long) */;
                    lock.unlock();
                    hasLock = false;
                    return println(arguments0Value_);
                }
                if (arguments0Value instanceof Boolean) {
                    boolean arguments0Value_ = (boolean) arguments0Value;
                    this.state_ = state | 0b100 /* add-active println(boolean) */;
                    lock.unlock();
                    hasLock = false;
                    return println(arguments0Value_);
                }
                if (arguments0Value instanceof String) {
                    String arguments0Value_ = (String) arguments0Value;
                    this.state_ = state | 0b1000 /* add-active println(String) */;
                    lock.unlock();
                    hasLock = false;
                    return println(arguments0Value_);
                }
                this.state_ = state | 0b10000 /* add-active println(Object) */;
                lock.unlock();
                hasLock = false;
                return println(arguments0Value);
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
            } else if (((state & 0b11110) & ((state & 0b11110) - 1)) == 0 /* is-single-active  */) {
                return NodeCost.MONOMORPHIC;
            }
            return NodeCost.POLYMORPHIC;
        }

    }
}
