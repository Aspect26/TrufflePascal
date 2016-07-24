package pascal.language.nodes;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;

import pascal.language.PascalLanguage;

public class PascalRootNode extends RootNode{
	@Child private ExpressionNode bodyNode;

    public PascalRootNode(FrameDescriptor frameDescriptor, ExpressionNode bodyNode) {
        super(PascalLanguage.class, null, frameDescriptor);
        this.bodyNode = bodyNode;
    }
    
    @Override
    public Object execute(VirtualFrame virtualFrame) {
    	return bodyNode.executeGeneric(virtualFrame);
    }
}
