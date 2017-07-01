// CheckStyle: start generated
package com.oracle.truffle.sl.nodes;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.InstrumentableFactory;
import com.oracle.truffle.api.instrumentation.ProbeNode;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(SLStatementNode.class)
public final class SLStatementNodeWrapper implements InstrumentableFactory<SLStatementNode> {

    @Override
    public WrapperNode createWrapper(SLStatementNode delegateNode, ProbeNode probeNode) {
        return new SLStatementNodeWrapper0(delegateNode, probeNode);
    }

    @GeneratedBy(SLStatementNode.class)
    private static final class SLStatementNodeWrapper0 extends SLStatementNode implements WrapperNode {

        @Child private SLStatementNode delegateNode;
        @Child private ProbeNode probeNode;

        private SLStatementNodeWrapper0(SLStatementNode delegateNode, ProbeNode probeNode) {
            this.delegateNode = delegateNode;
            this.probeNode = probeNode;
        }

        @Override
        public SLStatementNode getDelegateNode() {
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
