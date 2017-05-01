// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@GeneratedBy(InvokeNode.class)
public final class InvokeNodeGen extends InvokeNode {

    private InvokeNodeGen(FrameSlot subroutineSlot, ExpressionNode[] argumentNodes, TypeDescriptor type) {
        super(subroutineSlot, argumentNodes, type);
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        return this.invoke(frameValue);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    public static InvokeNode create(FrameSlot subroutineSlot, ExpressionNode[] argumentNodes, TypeDescriptor type) {
        return new InvokeNodeGen(subroutineSlot, argumentNodes, type);
    }

}
