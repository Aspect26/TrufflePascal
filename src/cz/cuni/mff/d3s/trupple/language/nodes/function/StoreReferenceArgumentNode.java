package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

public class StoreReferenceArgumentNode extends ExpressionNode {

	private final FrameSlot variableSlot;

	public StoreReferenceArgumentNode(FrameSlot variableSlot) {
		this.variableSlot = variableSlot;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return new Reference(frame, this.variableSlot);
	}
}
