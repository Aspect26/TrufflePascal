// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@GeneratedBy(FunctionBodyNode.class)
public final class FunctionBodyNodeGen extends FunctionBodyNode {

    private final FrameSlot slot;
    private final TypeDescriptor typeDescriptor;

    private FunctionBodyNodeGen(StatementNode bodyNode, FrameSlot slot, TypeDescriptor typeDescriptor) {
        super(bodyNode);
        this.slot = slot;
        this.typeDescriptor = typeDescriptor;
    }

    @Override
    protected FrameSlot getSlot() {
        return this.slot;
    }

    @Override
    protected TypeDescriptor getTypeDescriptor() {
        return this.typeDescriptor;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        return this.executeFunction(frameValue);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    public static FunctionBodyNode create(StatementNode bodyNode, FrameSlot slot, TypeDescriptor typeDescriptor) {
        return new FunctionBodyNodeGen(bodyNode, slot, typeDescriptor);
    }

}
