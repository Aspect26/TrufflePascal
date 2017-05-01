// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;

@GeneratedBy(GetMsCountNode.class)
public final class GetMsCountNodeGen extends GetMsCountNode {

    private GetMsCountNodeGen() {
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
        return this.getMiliseconds(frameValue);
    }

    public static GetMsCountNode create() {
        return new GetMsCountNodeGen();
    }

}
