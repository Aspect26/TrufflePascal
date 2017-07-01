// CheckStyle: start generated
package com.oracle.truffle.sl.nodes.interop;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLForeignToSLTypeNode.class)
public final class SLForeignToSLTypeNodeGen extends SLForeignToSLTypeNode {

    @CompilationFinal private int state_ = 1;

    private SLForeignToSLTypeNodeGen() {
    }

    @Override
    public Object executeConvert(VirtualFrame frameValue, Object arg0Value) {
        int state = state_;
        if ((state & 0b10) != 0 /* is-active fromObject(Number) */ && arg0Value instanceof Number) {
            Number arg0Value_ = (Number) arg0Value;
            return SLForeignToSLTypeNode.fromObject(arg0Value_);
        }
        if ((state & 0b100) != 0 /* is-active fromString(String) */ && arg0Value instanceof String) {
            String arg0Value_ = (String) arg0Value;
            return SLForeignToSLTypeNode.fromString(arg0Value_);
        }
        if ((state & 0b1000) != 0 /* is-active fromBoolean(boolean) */ && arg0Value instanceof Boolean) {
            boolean arg0Value_ = (boolean) arg0Value;
            return SLForeignToSLTypeNode.fromBoolean(arg0Value_);
        }
        if ((state & 0b10000) != 0 /* is-active fromChar(char) */ && arg0Value instanceof Character) {
            char arg0Value_ = (char) arg0Value;
            return SLForeignToSLTypeNode.fromChar(arg0Value_);
        }
        if ((state & 0b1100000) != 0 /* is-active unbox(VirtualFrame, TruffleObject) || fromTruffleObject(VirtualFrame, TruffleObject) */ && arg0Value instanceof TruffleObject) {
            TruffleObject arg0Value_ = (TruffleObject) arg0Value;
            if ((state & 0b100000) != 0 /* is-active unbox(VirtualFrame, TruffleObject) */) {
                if ((isBoxedPrimitive(frameValue, arg0Value_))) {
                    return unbox(frameValue, arg0Value_);
                }
            }
            if ((state & 0b1000000) != 0 /* is-active fromTruffleObject(VirtualFrame, TruffleObject) */) {
                if ((!(isBoxedPrimitive(frameValue, arg0Value_)))) {
                    return fromTruffleObject(frameValue, arg0Value_);
                }
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue, arg0Value);
    }

    private Object executeAndSpecialize(VirtualFrame frameValue, Object arg0Value) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            if (arg0Value instanceof Number) {
                Number arg0Value_ = (Number) arg0Value;
                this.state_ = state | 0b10 /* add-active fromObject(Number) */;
                lock.unlock();
                hasLock = false;
                return SLForeignToSLTypeNode.fromObject(arg0Value_);
            }
            if (arg0Value instanceof String) {
                String arg0Value_ = (String) arg0Value;
                this.state_ = state | 0b100 /* add-active fromString(String) */;
                lock.unlock();
                hasLock = false;
                return SLForeignToSLTypeNode.fromString(arg0Value_);
            }
            if (arg0Value instanceof Boolean) {
                boolean arg0Value_ = (boolean) arg0Value;
                this.state_ = state | 0b1000 /* add-active fromBoolean(boolean) */;
                lock.unlock();
                hasLock = false;
                return SLForeignToSLTypeNode.fromBoolean(arg0Value_);
            }
            if (arg0Value instanceof Character) {
                char arg0Value_ = (char) arg0Value;
                this.state_ = state | 0b10000 /* add-active fromChar(char) */;
                lock.unlock();
                hasLock = false;
                return SLForeignToSLTypeNode.fromChar(arg0Value_);
            }
            if (arg0Value instanceof TruffleObject) {
                TruffleObject arg0Value_ = (TruffleObject) arg0Value;
                if ((isBoxedPrimitive(frameValue, arg0Value_))) {
                    this.state_ = state | 0b100000 /* add-active unbox(VirtualFrame, TruffleObject) */;
                    lock.unlock();
                    hasLock = false;
                    return unbox(frameValue, arg0Value_);
                }
                if ((!(isBoxedPrimitive(frameValue, arg0Value_)))) {
                    this.state_ = state | 0b1000000 /* add-active fromTruffleObject(VirtualFrame, TruffleObject) */;
                    lock.unlock();
                    hasLock = false;
                    return fromTruffleObject(frameValue, arg0Value_);
                }
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new UnsupportedSpecializationException(this, new Node[] {});
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
        } else if (((state & 0b1111110) & ((state & 0b1111110) - 1)) == 0 /* is-single-active  */) {
            return NodeCost.MONOMORPHIC;
        }
        return NodeCost.POLYMORPHIC;
    }

    public static SLForeignToSLTypeNode create() {
        return new SLForeignToSLTypeNodeGen();
    }

}
