package cz.cuni.mff.d3s.trupple.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

public class StringLiteralNode extends ExpressionNode {

	private final PascalString value;

	public StringLiteralNode(String value) {
		this.value = new PascalString(value);
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
}
