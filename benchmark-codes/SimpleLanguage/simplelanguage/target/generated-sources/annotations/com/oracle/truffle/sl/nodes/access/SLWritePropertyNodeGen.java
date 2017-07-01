// CheckStyle: start generated
package com.oracle.truffle.sl.nodes.access;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import java.util.concurrent.locks.Lock;

@GeneratedBy(SLWritePropertyNode.class)
public final class SLWritePropertyNodeGen extends SLWritePropertyNode {

    @Child private SLExpressionNode receiverNode_;
    @Child private SLExpressionNode nameNode_;
    @Child private SLExpressionNode valueNode_;
    @CompilationFinal private int state_ = 1;

    private SLWritePropertyNodeGen(SLExpressionNode receiverNode, SLExpressionNode nameNode, SLExpressionNode valueNode) {
        this.receiverNode_ = receiverNode;
        this.nameNode_ = nameNode;
        this.valueNode_ = valueNode;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        Object receiverNodeValue_ = receiverNode_.executeGeneric(frameValue);
        Object nameNodeValue_ = nameNode_.executeGeneric(frameValue);
        Object valueNodeValue_ = valueNode_.executeGeneric(frameValue);
        if ((state & 0b10) != 0 /* is-active write(VirtualFrame, Object, Object, Object) */) {
            return write(frameValue, receiverNodeValue_, nameNodeValue_, valueNodeValue_);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(frameValue, receiverNodeValue_, nameNodeValue_, valueNodeValue_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    private Object executeAndSpecialize(VirtualFrame frameValue, Object receiverNodeValue, Object nameNodeValue, Object valueNodeValue) {
        Lock lock = getLock();
        boolean hasLock = true;
        lock.lock();
        try {
            int state = state_ & 0xfffffffe/* mask-active uninitialized*/;
            this.state_ = state | 0b10 /* add-active write(VirtualFrame, Object, Object, Object) */;
            lock.unlock();
            hasLock = false;
            return write(frameValue, receiverNodeValue, nameNodeValue, valueNodeValue);
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

    public static SLWritePropertyNode create(SLExpressionNode receiverNode, SLExpressionNode nameNode, SLExpressionNode valueNode) {
        return new SLWritePropertyNodeGen(receiverNode, nameNode, valueNode);
    }

}
