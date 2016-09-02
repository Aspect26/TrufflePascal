package cz.cuni.mff.d3s.trupple.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.customvalues.ICustomValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

public class CustomTypeLiteralNode extends ExpressionNode{

	private final ICustomValue variable;
	
	public CustomTypeLiteralNode(ICustomValue customValue) {
		this.variable = customValue;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return variable.getValue();
	}
}
