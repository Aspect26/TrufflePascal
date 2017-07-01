// CheckStyle: start generated
package com.oracle.truffle.sl.nodes.access;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLReadPropertyNode.class)
public final class SLReadPropertyNodeGen extends SLReadPropertyNode {

    @Child private SLExpressionNode receiverNode_;
    @Child private SLExpressionNode nameNode_;
    @CompilationFinal private int state_ = 1;

    private SLReadPropertyNodeGen(SLExpressionNode receiverNode, SLExpressionNode nameNode) {
        this.receiverNode_ = receiverNode;
        this.nameNode_ = nameNode;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        Object receiverNodeValue_ = receiverNode_.executeGeneric(frameValue);
        Object nameNodeValue_ = nameNode_.executeGeneric(frameValue);
        if ((state & 0b10) != 0 /* is-active read(VirtualFrame, Object, Object) */) {
            return read(frameValue, receiverNodeValue_, nameNodeValue_);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue, receiverNodeValue_, nameNodeValue_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    private Object executeAndSpecialize(VirtualFrame frameValue, Object receiverNodeValue, Object nameNodeValue) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            this.state_ = state | 0b10 /* add-active read(VirtualFrame, Object, Object) */;
            lock.unlock();
            hasLock = false;
            return read(frameValue, receiverNodeValue, nameNodeValue);
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

    public static SLReadPropertyNode create(SLExpressionNode receiverNode, SLExpressionNode nameNode) {
        return new SLReadPropertyNodeGen(receiverNode, nameNode);
    }

}
