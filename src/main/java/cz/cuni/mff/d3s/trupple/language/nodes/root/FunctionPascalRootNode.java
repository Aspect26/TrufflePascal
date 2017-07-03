package cz.cuni.mff.d3s.trupple.language.nodes.root;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

/**
 * Node representing the root node of a function's AST.
 */
public class FunctionPascalRootNode extends PascalRootNode {

    public FunctionPascalRootNode(FrameDescriptor frameDescriptor, ExpressionNode bodyNode) {
        super(frameDescriptor, bodyNode);
    }

}
