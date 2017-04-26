package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class StoreReferenceArgumentNode extends ExpressionNode {

	private final FrameSlot variableSlot;

	private final TypeDescriptor typeDescriptor;

	public StoreReferenceArgumentNode(FrameSlot variableSlot, TypeDescriptor typeDescriptor) {
		this.variableSlot = variableSlot;
        this.typeDescriptor = typeDescriptor;
    }

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return new Reference(frame, this.variableSlot);
	}

	@Override
    public TypeDescriptor getType() {
	    return this.typeDescriptor;
    }

}
