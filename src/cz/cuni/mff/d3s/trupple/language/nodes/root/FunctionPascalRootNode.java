package cz.cuni.mff.d3s.trupple.language.nodes.root;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

public class FunctionPascalRootNode extends PascalRootNode {

    public FunctionPascalRootNode(FrameDescriptor frameDescriptor, ExpressionNode bodyNode) {
        super(frameDescriptor, bodyNode);
    }

}
