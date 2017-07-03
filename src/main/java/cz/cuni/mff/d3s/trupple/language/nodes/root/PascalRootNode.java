package cz.cuni.mff.d3s.trupple.language.nodes.root;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.PascalTypes;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

/**
 * This node represents the root node of AST of any subroutine or main program.
 */
@TypeSystemReference(PascalTypes.class)
public abstract class PascalRootNode extends RootNode {

	@Child
	private ExpressionNode bodyNode;

	public PascalRootNode(FrameDescriptor frameDescriptor, ExpressionNode bodyNode) {
		super(PascalLanguage.class, null, frameDescriptor);
		this.bodyNode = bodyNode;
	}

	public Object execute(VirtualFrame virtualFrame) {
		return bodyNode.executeGeneric(virtualFrame);
	}

}
