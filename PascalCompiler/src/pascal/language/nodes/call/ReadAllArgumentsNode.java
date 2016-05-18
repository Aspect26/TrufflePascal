package pascal.language.nodes.call;

import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.language.nodes.ExpressionNode;

public class ReadAllArgumentsNode extends ExpressionNode {

	@Override
	public Object[] executeGeneric(VirtualFrame frame){
		return frame.getArguments();
	}
}
