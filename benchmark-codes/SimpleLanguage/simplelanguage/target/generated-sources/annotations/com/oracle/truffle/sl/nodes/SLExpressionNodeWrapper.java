// CheckStyle: start generated
package com.oracle.truffle.sl.nodes;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.InstrumentableFactory;
import com.oracle.truffle.api.instrumentation.ProbeNode;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

@GeneratedBy(SLExpressionNode.class)
public final class SLExpressionNodeWrapper implements InstrumentableFactory<SLExpressionNode> {

    @Override
    public WrapperNode createWrapper(SLExpressionNode delegateNode, ProbeNode probeNode) {
        return new SLExpressionNodeWrapper0(delegateNode, probeNode);
    }

    @GeneratedBy(SLExpressionNode.class)
    private static final class SLExpressionNodeWrapper0 extends SLExpressionNode implements WrapperNode {

        @Child private SLExpressionNode delegateNode;
        @Child private ProbeNode probeNode;

        private SLExpressionNodeWrapper0(SLExpressionNode delegateNode, ProbeNode probeNode) {
            this.delegateNode = delegateNode;
            this.probeNode = probeNode;
        }

        @Override
        public SLExpressionNode getDelegateNode() {
            return delegateNode;
        }

        @Override
        public ProbeNode getProbeNode() {
            return probeNode;
        }

        @Override
        public NodeCost getCost() {
            return NodeCost.NONE;
        }

        @Override
        public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
            try {
                probeNode.onEnter(frame);
                boolean returnValue = delegateNode.executeBoolean(frame);
                probeNode.onReturnValue(frame, returnValue);
                return returnValue;
            } catch (Throwable t) {
                probeNode.onReturnExceptional(frame, t);
                throw t;
            }
        }

        @Override
        public Object executeGeneric(VirtualFrame frame) {
            try {
                probeNode.onEnter(frame);
                Object returnValue = delegateNode.executeGeneric(frame);
                probeNode.onReturnValue(frame, returnValue);
                return returnValue;
            } catch (Throwable t) {
                probeNode.onReturnExceptional(frame, t);
                throw t;
            }
        }

        @Override
        public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
            try {
                probeNode.onEnter(frame);
                long returnValue = delegateNode.executeLong(frame);
                probeNode.onReturnValue(frame, returnValue);
                return returnValue;
            } catch (Throwable t) {
                probeNode.onReturnExceptional(frame, t);
                throw t;
            }
        }

        @Override
        public void executeVoid(VirtualFrame frame) {
            try {
                probeNode.onEnter(frame);
                delegateNode.executeVoid(frame);
                probeNode.onReturnValue(frame, null);
            } catch (Throwable t) {
                probeNode.onReturnExceptional(frame, t);
                throw t;
            }
        }

    }
}
