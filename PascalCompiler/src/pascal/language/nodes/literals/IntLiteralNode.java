package pascal.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.language.nodes.ExpressionNode;

public class IntLiteralNode extends ExpressionNode {
	
	private final int value;
	
	public IntLiteralNode(int value){
		this.value = value;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
}
