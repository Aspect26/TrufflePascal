package pascal.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.language.nodes.ExpressionNode;

public class DoubleLiteralNode extends ExpressionNode {
	
	private final double value;
	
	public DoubleLiteralNode(double value){
		this.value = value;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
}
