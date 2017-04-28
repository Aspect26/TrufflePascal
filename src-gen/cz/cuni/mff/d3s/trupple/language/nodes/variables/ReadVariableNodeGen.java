// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeCost;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@GeneratedBy(ReadVariableNode.class)
public final class ReadVariableNodeGen extends ReadVariableNode {

    private final FrameSlot slot;
    private final TypeDescriptor typeDescriptor;

    private ReadVariableNodeGen(FrameSlot slot, TypeDescriptor typeDescriptor) {
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
        return this.readObject(frameValue);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    public static ReadVariableNode create(FrameSlot slot, TypeDescriptor typeDescriptor) {
        return new ReadVariableNodeGen(slot, typeDescriptor);
    }

}
