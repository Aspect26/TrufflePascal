package cz.cuni.mff.d3s.trupple.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

public class DoubleLiteralNode extends ExpressionNode {

	private final double value;

	public DoubleLiteralNode(double value) {
		this.value = value;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
}
