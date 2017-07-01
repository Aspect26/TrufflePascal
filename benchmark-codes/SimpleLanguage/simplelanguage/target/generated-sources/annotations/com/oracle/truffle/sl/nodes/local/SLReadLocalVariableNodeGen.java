// CheckStyle: start generated
package com.oracle.truffle.sl.nodes.local;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLReadLocalVariableNode.class)
public final class SLReadLocalVariableNodeGen extends SLReadLocalVariableNode {

    private final FrameSlot slot;
    @CompilationFinal private int state_ = 1;
    @CompilationFinal private int exclude_;

    private SLReadLocalVariableNodeGen(FrameSlot slot) {
        this.slot = slot;
    }

    @Override
    protected FrameSlot getSlot() {
        return this.slot;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b10) != 0 /* is-active readLong(VirtualFrame) */) {
            if ((isLong(frameValue))) {
                return readLong(frameValue);
            }
        }
        if ((state & 0b100) != 0 /* is-active readBoolean(VirtualFrame) */) {
            if ((isBoolean(frameValue))) {
                return readBoolean(frameValue);
            }
        }
        if ((state & 0b1000) != 0 /* is-active readObject(VirtualFrame) */) {
            return readObject(frameValue);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b1000) != 0 /* is-active readObject(VirtualFrame) */) {
            return SLTypesGen.expectBoolean(executeGeneric(frameValue));
        }
        if ((state & 0b100) != 0 /* is-active readBoolean(VirtualFrame) */) {
            if ((isBoolean(frameValue))) {
                return readBoolean(frameValue);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectBoolean(executeAndSpecialize(frameValue));
    }

    @Override
    public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
        int state = state_;
        if ((state & 0b1000) != 0 /* is-active readObject(VirtualFrame) */) {
            return SLTypesGen.expectLong(executeGeneric(frameValue));
        }
        if ((state & 0b10) != 0 /* is-active readLong(VirtualFrame) */) {
            if ((isLong(frameValue))) {
                return readLong(frameValue);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return SLTypesGen.expectLong(executeAndSpecialize(frameValue));
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        int state = state_;
        try {
            if ((state & 0b1101) == 0 /* only-active readLong(VirtualFrame) */) {
                executeLong(frameValue);
                return;
            } else if ((state & 0b1011) == 0 /* only-active readBoolean(VirtualFrame) */) {
                executeBoolean(frameValue);
                return;
            }
            executeGeneric(frameValue);
            return;
        } catch (UnexpectedResultException ex) {
            return;
        }
    }

    private Object executeAndSpecialize(VirtualFrame frameValue) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            int exclude = exclude_;
            if ((exclude & 0b1) == 0 /* is-not-excluded readLong(VirtualFrame) */) {
                if ((isLong(frameValue))) {
                    this.state_ = state | 0b10 /* add-active readLong(VirtualFrame) */;
                    lock.unlock();
                    hasLock = false;
                    return readLong(frameValue);
                }
            }
            if ((exclude & 0b10) == 0 /* is-not-excluded readBoolean(VirtualFrame) */) {
                if ((isBoolean(frameValue))) {
                    this.state_ = state | 0b100 /* add-active readBoolean(VirtualFrame) */;
                    lock.unlock();
                    hasLock = false;
                    return readBoolean(frameValue);
                }
            }
            this.exclude_ = exclude | 0b11 /* add-excluded readLong(VirtualFrame), readBoolean(VirtualFrame) */;
            state = state & 0xfffffff9 /* remove-active readLong(VirtualFrame), readBoolean(VirtualFrame) */;
            this.state_ = state | 0b1000 /* add-active readObject(VirtualFrame) */;
            lock.unlock();
            hasLock = false;
            return readObject(frameValue);
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
        } else if (((state & 0b1110) & ((state & 0b1110) - 1)) == 0 /* is-single-active  */) {
            return NodeCost.MONOMORPHIC;
        }
        return NodeCost.POLYMORPHIC;
    }

    public static SLReadLocalVariableNode create(FrameSlot slot) {
        return new SLReadLocalVariableNodeGen(slot);
    }

}
