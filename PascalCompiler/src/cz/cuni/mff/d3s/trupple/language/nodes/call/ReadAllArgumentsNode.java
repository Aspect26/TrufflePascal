package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

public class ReadAllArgumentsNode extends ExpressionNode {

	@Override
	public Object[] executeGeneric(VirtualFrame frame) {
		return frame.getArguments();
	}
}
