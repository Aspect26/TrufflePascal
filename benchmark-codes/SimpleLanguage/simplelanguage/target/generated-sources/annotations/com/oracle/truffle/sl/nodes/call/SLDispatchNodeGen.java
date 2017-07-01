// CheckStyle: start generated
package com.oracle.truffle.sl.nodes.call;

import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.ExplodeLoop.LoopExplosionKind;
import com.oracle.truffle.sl.nodes.interop.SLForeignToSLTypeNode;
import com.oracle.truffle.sl.runtime.SLFunction;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLDispatchNode.class)
public final class SLDispatchNodeGen extends SLDispatchNode {

    @CompilationFinal private int state_ = 1;
    @CompilationFinal private int exclude_;
    @Child private DirectData direct_cache;
    @Child private IndirectCallNode indirect_callNode_;
    @Child private Node foreign_crossLanguageCallNode_;
    @Child private SLForeignToSLTypeNode foreign_toSLTypeNode_;

    private SLDispatchNodeGen() {
    }

    @ExplodeLoop(kind = LoopExplosionKind.FULL_EXPLODE_UNTIL_RETURN)
    @SuppressWarnings("unused")
    private boolean fallbackGuard_(Object arg0Value, Object[] arg1Value) {
        if (arg0Value instanceof SLFunction) {
            DirectData s1_ = direct_cache;
            while (s1_ != null) {
                {
                    SLFunction arg0Value_ = (SLFunction) arg0Value;
                    // assert (arg0Value_ == s1_.cachedFunction_);
                    if (isValid_(s1_.assumption0_)) {
                        return false;
                    }
                }
                s1_ = s1_.next_;
            }
            return false;
        }
        if (arg0Value instanceof TruffleObject) {
            TruffleObject arg0Value_ = (TruffleObject) arg0Value;
            if ((SLDispatchNode.isForeignFunction(arg0Value_))) {
                return false;
            }
        }
        return true;
    }

    @ExplodeLoop(kind = LoopExplosionKind.FULL_EXPLODE_UNTIL_RETURN)
    @Override
    public Object executeDispatch(VirtualFrame frameValue, Object arg0Value, Object[] arg1Value) {
        int state = state_;
        if ((state & 0b11110) != 0 /* is-active doDirect(VirtualFrame, SLFunction, Object[], SLFunction, DirectCallNode) || doIndirect(VirtualFrame, SLFunction, Object[], IndirectCallNode) || doForeign(VirtualFrame, TruffleObject, Object[], Node, SLForeignToSLTypeNode) || unknownFunction(Object, Object[]) */) {
            if ((state & 0b110) != 0 /* is-active doDirect(VirtualFrame, SLFunction, Object[], SLFunction, DirectCallNode) || doIndirect(VirtualFrame, SLFunction, Object[], IndirectCallNode) */ && arg0Value instanceof SLFunction) {
                SLFunction arg0Value_ = (SLFunction) arg0Value;
                if ((state & 0b10) != 0 /* is-active doDirect(VirtualFrame, SLFunction, Object[], SLFunction, DirectCallNode) */) {
                    DirectData s1_ = direct_cache;
                    while (s1_ != null) {
                        if (!isValid_(s1_.assumption0_)) {
                            CompilerDirectives.transferToInterpreterAndInvalidate();
                            removeDirect_(s1_);
                            return executeAndSpecialize(frameValue, arg0Value_, arg1Value);
                        }
                        if ((arg0Value_ == s1_.cachedFunction_)) {
                            return SLDispatchNode.doDirect(frameValue, arg0Value_, arg1Value, s1_.cachedFunction_, s1_.callNode_);
                        }
                        s1_ = s1_.next_;
                    }
                }
                if ((state & 0b100) != 0 /* is-active doIndirect(VirtualFrame, SLFunction, Object[], IndirectCallNode) */) {
                    return SLDispatchNode.doIndirect(frameValue, arg0Value_, arg1Value, indirect_callNode_);
                }
            }
            if ((state & 0b1000) != 0 /* is-active doForeign(VirtualFrame, TruffleObject, Object[], Node, SLForeignToSLTypeNode) */ && arg0Value instanceof TruffleObject) {
                TruffleObject arg0Value_ = (TruffleObject) arg0Value;
                if ((SLDispatchNode.isForeignFunction(arg0Value_))) {
                    return SLDispatchNode.doForeign(frameValue, arg0Value_, arg1Value, foreign_crossLanguageCallNode_, foreign_toSLTypeNode_);
                }
            }
            if ((state & 0b10000) != 0 /* is-active unknownFunction(Object, Object[]) */) {
                if (fallbackGuard_(arg0Value, arg1Value)) {
                    return SLDispatchNode.unknownFunction(arg0Value, arg1Value);
                }
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue, arg0Value, arg1Value);
    }

    private Object executeAndSpecialize(VirtualFrame frameValue, Object arg0Value, Object[] arg1Value) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            int exclude = exclude_;
            if (arg0Value instanceof SLFunction) {
                SLFunction arg0Value_ = (SLFunction) arg0Value;
                if ((exclude & 0b1) == 0 /* is-not-excluded doDirect(VirtualFrame, SLFunction, Object[], SLFunction, DirectCallNode) */) {
                    int count1_ = 0;
                    DirectData s1_ = direct_cache;
                    if ((state & 0b10) != 0 /* is-active doDirect(VirtualFrame, SLFunction, Object[], SLFunction, DirectCallNode) */) {
                        while (s1_ != null) {
                            if ((arg0Value_ == s1_.cachedFunction_) && isValid_(s1_.assumption0_)) {
                                break;
                            }
                            s1_ = s1_.next_;
                            count1_++;
                        }
                    }
                    if (s1_ == null) {
                        {
                            SLFunction cachedFunction = (arg0Value_);
                            // assert (arg0Value_ == cachedFunction);
                            Assumption assumption0 = (cachedFunction.getCallTargetStable());
                            if (isValid_(assumption0)) {
                                if (count1_ < (SLDispatchNode.INLINE_CACHE_SIZE)) {
                                    DirectCallNode callNode = (DirectCallNode.create(cachedFunction.getCallTarget()));
                                    s1_ = new DirectData(direct_cache, cachedFunction, callNode, assumption0);
                                    this.direct_cache = super.insert(s1_);
                                    this.state_ = state | 0b10 /* add-active doDirect(VirtualFrame, SLFunction, Object[], SLFunction, DirectCallNode) */;
                                }
                            }
                        }
                    }
                    if (s1_ != null) {
                        lock.unlock();
                        hasLock = false;
                        return SLDispatchNode.doDirect(frameValue, arg0Value_, arg1Value, s1_.cachedFunction_, s1_.callNode_);
                    }
                }
                this.indirect_callNode_ = super.insert((IndirectCallNode.create()));
                this.exclude_ = exclude | 0b1 /* add-excluded doDirect(VirtualFrame, SLFunction, Object[], SLFunction, DirectCallNode) */;
                this.direct_cache = null;
                state = state & 0xfffffffd /* remove-active doDirect(VirtualFrame, SLFunction, Object[], SLFunction, DirectCallNode) */;
                this.state_ = state | 0b100 /* add-active doIndirect(VirtualFrame, SLFunction, Object[], IndirectCallNode) */;
                lock.unlock();
                hasLock = false;
                return SLDispatchNode.doIndirect(frameValue, arg0Value_, arg1Value, indirect_callNode_);
            }
            if (arg0Value instanceof TruffleObject) {
                TruffleObject arg0Value_ = (TruffleObject) arg0Value;
                if ((SLDispatchNode.isForeignFunction(arg0Value_))) {
                    this.foreign_crossLanguageCallNode_ = super.insert((SLDispatchNode.createCrossLanguageCallNode(arg1Value)));
                    this.foreign_toSLTypeNode_ = super.insert((SLDispatchNode.createToSLTypeNode()));
                    this.state_ = state | 0b1000 /* add-active doForeign(VirtualFrame, TruffleObject, Object[], Node, SLForeignToSLTypeNode) */;
                    lock.unlock();
                    hasLock = false;
                    return SLDispatchNode.doForeign(frameValue, arg0Value_, arg1Value, foreign_crossLanguageCallNode_, foreign_toSLTypeNode_);
                }
            }
            this.state_ = state | 0b10000 /* add-active unknownFunction(Object, Object[]) */;
            lock.unlock();
            hasLock = false;
            return SLDispatchNode.unknownFunction(arg0Value, arg1Value);
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
            DirectData s1_ = this.direct_cache;
            if ((s1_ == null || s1_.next_ == null)) {
                return NodeCost.MONOMORPHIC;
            }
        }
        return NodeCost.POLYMORPHIC;
    }

    void removeDirect_(Object s1_) {
        Lock lock = getLock();
        lock.lock();
        try {
            DirectData prev = null;
            DirectData cur = this.direct_cache;
            while (cur != null) {
                if (cur == s1_) {
                    if (prev == null) {
                        this.direct_cache = cur.next_;
                        this.adoptChildren();
                    } else {
                        prev.next_ = cur.next_;
                        prev.adoptChildren();
                    }
                    break;
                }
                prev = cur;
                cur = cur.next_;
            }
            if (this.direct_cache == null) {
                this.state_ = this.state_ & 0xfffffffd /* remove-active doDirect(VirtualFrame, SLFunction, Object[], SLFunction, DirectCallNode) */;
            }
        } finally {
            lock.unlock();
        }
    }

    private static boolean isValid_(Assumption assumption) {
        return assumption != null && assumption.isValid();
    }

    public static SLDispatchNode create() {
        return new SLDispatchNodeGen();
    }

    @GeneratedBy(SLDispatchNode.class)
    private static final class DirectData extends Node {

        @Child DirectData next_;
        final SLFunction cachedFunction_;
        @Child DirectCallNode callNode_;
        final Assumption assumption0_;

        DirectData(DirectData next_, SLFunction cachedFunction_, DirectCallNode callNode_, Assumption assumption0_) {
            this.next_ = next_;
            this.cachedFunction_ = cachedFunction_;
            this.callNode_ = callNode_;
            this.assumption0_ = assumption0_;
        }

        @Override
        public NodeCost getCost() {
            return NodeCost.NONE;
        }

    }
}
