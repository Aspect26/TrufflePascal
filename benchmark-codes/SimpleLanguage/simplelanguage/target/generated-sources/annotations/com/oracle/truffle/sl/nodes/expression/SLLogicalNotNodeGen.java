// CheckStyle: start generated
package com.oracle.truffle.sl.nodes.expression;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLLogicalNotNode.class)
public final class SLLogicalNotNodeGen extends SLLogicalNotNode {

    @Child private SLExpressionNode valueNode_;
    @CompilationFinal private int state_ = 1;

    private SLLogicalNotNodeGen(SLExpressionNode valueNode) {
        this.valueNode_ = valueNode;
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) {
        int state = state_;
        boolean valueNodeValue_;
        try {
            valueNodeValue_ = valueNode_.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(ex.getResult());
        }
        if ((state & 0b10) != 0 /* is-active doBoolean(boolean) */) {
            return doBoolean(valueNodeValue_);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(valueNodeValue_);
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        return executeBoolean(frameValue);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeBoolean(frameValue);
        return;
    }

    private boolean executeAndSpecialize(Object valueNodeValue) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            if (valueNodeValue instanceof Boolean) {
                boolean valueNodeValue_ = (boolean) valueNodeValue;
                this.state_ = state | 0b10 /* add-active doBoolean(boolean) */;
                lock.unlock();
                hasLock = false;
                return doBoolean(valueNodeValue_);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new UnsupportedSpecializationException(this, new Node[] {this.valueNode_}, valueNodeValue);
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

    public static SLLogicalNotNode create(SLExpressionNode valueNode) {
        return new SLLogicalNotNodeGen(valueNode);
    }

}
