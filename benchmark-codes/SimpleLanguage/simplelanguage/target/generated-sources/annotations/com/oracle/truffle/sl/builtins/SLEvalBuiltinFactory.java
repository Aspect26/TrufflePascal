// CheckStyle: start generated
package com.oracle.truffle.sl.builtins;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.ExplodeLoop.LoopExplosionKind;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.runtime.SLContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLEvalBuiltin.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class SLEvalBuiltinFactory implements NodeFactory<SLEvalBuiltin> {

    private static SLEvalBuiltinFactory instance;

    private SLEvalBuiltinFactory() {
    }

    @Override
    public Class<SLEvalBuiltin> getNodeClass() {
        return SLEvalBuiltin.class;
    }

    @Override
    public List getExecutionSignature() {
        return Arrays.asList(SLExpressionNode.class, SLExpressionNode.class);
    }

    @Override
    public List getNodeSignatures() {
        return Arrays.asList(Arrays.asList(SLExpressionNode[].class, SLContext.class));
    }

    @Override
    public SLEvalBuiltin createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof SLExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof SLContext)) {
            return create((SLExpressionNode[]) arguments[0], (SLContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<SLEvalBuiltin> getInstance() {
        if (instance == null) {
            instance = new SLEvalBuiltinFactory();
        }
        return instance;
    }

    public static SLEvalBuiltin create(SLExpressionNode[] arguments, SLContext context) {
        return new SLEvalBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(SLEvalBuiltin.class)
    public static final class SLEvalBuiltinNodeGen extends SLEvalBuiltin {

        private final SLContext context;
        @Child private SLExpressionNode arguments0_;
        @Child private SLExpressionNode arguments1_;
        @CompilationFinal private int state_ = 1;
        @CompilationFinal private int exclude_;
        @Child private EvalCachedData evalCached_cache;

        private SLEvalBuiltinNodeGen(SLExpressionNode[] arguments, SLContext context) {
            this.context = context;
            this.arguments0_ = arguments != null && 0 < arguments.length ? arguments[0] : null;
            this.arguments1_ = arguments != null && 1 < arguments.length ? arguments[1] : null;
        }

        @Override
        public SLContext getContext() {
            return this.context;
        }

        @ExplodeLoop(kind = LoopExplosionKind.FULL_EXPLODE_UNTIL_RETURN)
        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            int state = state_;
            Object arguments0Value_ = arguments0_.executeGeneric(frameValue);
            Object arguments1Value_ = arguments1_.executeGeneric(frameValue);
            if ((state & 0b110) != 0 /* is-active evalCached(VirtualFrame, String, String, String, String, DirectCallNode) || evalUncached(String, String) */ && arguments0Value_ instanceof String) {
                String arguments0Value__ = (String) arguments0Value_;
                if (arguments1Value_ instanceof String) {
                    String arguments1Value__ = (String) arguments1Value_;
                    if ((state & 0b10) != 0 /* is-active evalCached(VirtualFrame, String, String, String, String, DirectCallNode) */) {
                        EvalCachedData s1_ = evalCached_cache;
                        while (s1_ != null) {
                            if ((SLEvalBuiltin.stringsEqual(s1_.cachedMimeType_, arguments0Value__)) && (SLEvalBuiltin.stringsEqual(s1_.cachedCode_, arguments1Value__))) {
                                return evalCached(frameValue, arguments0Value__, arguments1Value__, s1_.cachedMimeType_, s1_.cachedCode_, s1_.callNode_);
                            }
                            s1_ = s1_.next_;
                        }
                    }
                    if ((state & 0b100) != 0 /* is-active evalUncached(String, String) */) {
                        return evalUncached(arguments0Value__, arguments1Value__);
                    }
                }
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            return executeAndSpecialize(frameValue, arguments0Value_, arguments1Value_);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            executeGeneric(frameValue);
            return;
        }

        private Object executeAndSpecialize(VirtualFrame frameValue, Object arguments0Value, Object arguments1Value) {
            Lock lock = getLock();
            boolean hasLock = true;
            lock.lock();
            try {
                int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
                int exclude = exclude_;
                if (arguments0Value instanceof String) {
                    String arguments0Value_ = (String) arguments0Value;
                    if (arguments1Value instanceof String) {
                        String arguments1Value_ = (String) arguments1Value;
                        if ((exclude & 0b1) == 0 /* is-not-excluded evalCached(VirtualFrame, String, String, String, String, DirectCallNode) */) {
                            int count1_ = 0;
                            EvalCachedData s1_ = evalCached_cache;
                            if ((state & 0b10) != 0 /* is-active evalCached(VirtualFrame, String, String, String, String, DirectCallNode) */) {
                                while (s1_ != null) {
                                    if ((SLEvalBuiltin.stringsEqual(s1_.cachedMimeType_, arguments0Value_)) && (SLEvalBuiltin.stringsEqual(s1_.cachedCode_, arguments1Value_))) {
                                        break;
                                    }
                                    s1_ = s1_.next_;
                                    count1_++;
                                }
                            }
                            if (s1_ == null) {
                                {
                                    String cachedMimeType = (arguments0Value_);
                                    if ((SLEvalBuiltin.stringsEqual(cachedMimeType, arguments0Value_))) {
                                        String cachedCode = (arguments1Value_);
                                        if ((SLEvalBuiltin.stringsEqual(cachedCode, arguments1Value_)) && count1_ < (3)) {
                                            DirectCallNode callNode = (DirectCallNode.create(parse(arguments0Value_, arguments1Value_)));
                                            s1_ = new EvalCachedData(evalCached_cache, cachedMimeType, cachedCode, callNode);
                                            this.evalCached_cache = super.insert(s1_);
                                            this.state_ = state | 0b10 /* add-active evalCached(VirtualFrame, String, String, String, String, DirectCallNode) */;
                                        }
                                    }
                                }
                            }
                            if (s1_ != null) {
                                lock.unlock();
                                hasLock = false;
                                return evalCached(frameValue, arguments0Value_, arguments1Value_, s1_.cachedMimeType_, s1_.cachedCode_, s1_.callNode_);
                            }
                        }
                        this.exclude_ = exclude | 0b1 /* add-excluded evalCached(VirtualFrame, String, String, String, String, DirectCallNode) */;
                        this.evalCached_cache = null;
                        state = state & 0xfffffffd /* remove-active evalCached(VirtualFrame, String, String, String, String, DirectCallNode) */;
                        this.state_ = state | 0b100 /* add-active evalUncached(String, String) */;
                        lock.unlock();
                        hasLock = false;
                        return evalUncached(arguments0Value_, arguments1Value_);
                    }
                }
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new UnsupportedSpecializationException(this, new Node[] {this.arguments0_, this.arguments1_}, arguments0Value, arguments1Value);
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
            } else if (((state & 0b110) & ((state & 0b110) - 1)) == 0 /* is-single-active  */) {
                EvalCachedData s1_ = this.evalCached_cache;
                if ((s1_ == null || s1_.next_ == null)) {
                    return NodeCost.MONOMORPHIC;
                }
            }
            return NodeCost.POLYMORPHIC;
        }

        @GeneratedBy(SLEvalBuiltin.class)
        private static final class EvalCachedData extends Node {

            @Child EvalCachedData next_;
            final String cachedMimeType_;
            final String cachedCode_;
            @Child DirectCallNode callNode_;

            EvalCachedData(EvalCachedData next_, String cachedMimeType_, String cachedCode_, DirectCallNode callNode_) {
                this.next_ = next_;
                this.cachedMimeType_ = cachedMimeType_;
                this.cachedCode_ = cachedCode_;
                this.callNode_ = callNode_;
            }

            @Override
            public NodeCost getCost() {
                return NodeCost.NONE;
            }

        }
    }
}
