package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;

public class PascalRootNode extends RootNode {
	@Child
	private ExpressionNode bodyNode;

	public PascalRootNode(FrameDescriptor frameDescriptor, ExpressionNode bodyNode) {
		super(PascalLanguage.class, null, frameDescriptor);
		this.bodyNode = bodyNode;
	}

	@Override
	public Object execute(VirtualFrame virtualFrame) {
		return bodyNode.executeGeneric(virtualFrame);
	}
}
