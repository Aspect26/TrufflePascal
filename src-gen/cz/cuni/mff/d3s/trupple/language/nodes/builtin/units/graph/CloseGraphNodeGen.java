// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(CloseGraphNode.class)
public final class CloseGraphNodeGen extends CloseGraphNode {

    private CloseGraphNodeGen() {
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        return executeLong(frameValue);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeLong(frameValue);
        return;
    }

    @Override
    public long executeLong(VirtualFrame frameValue) {
        return this.closeGraph(frameValue);
    }

    public static CloseGraphNode create() {
        return new CloseGraphNodeGen();
    }

}
