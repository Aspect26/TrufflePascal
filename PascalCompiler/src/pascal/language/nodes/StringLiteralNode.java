package pascal.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class StringLiteralNode extends ExpressionNode {
	
	private final String value;
	
	public StringLiteralNode(String value){
		this.value = value;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return value;
	}
}
