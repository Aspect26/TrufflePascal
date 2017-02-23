package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

public class ReadReferencePassNode extends ExpressionNode {

	private final String variableIdentifier;

	public ReadReferencePassNode(String variableIdentifier) {
		this.variableIdentifier = variableIdentifier;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return new Reference(frame, this.variableIdentifier);
	}
}
