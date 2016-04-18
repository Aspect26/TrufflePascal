package pascal.language.nodes;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RootNode;

public class PascalRootNode extends RootNode{
	@Children private final PascalNode[] bodyNodes;

    public PascalRootNode(PascalNode[] bodyNodes, FrameDescriptor frameDescriptor) {
        super(null, null, frameDescriptor);
        this.bodyNodes = bodyNodes;
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame virtualFrame) {
        int count = this.bodyNodes.length - 1;
        CompilerAsserts.compilationConstant(count);
        for (int i=0; i<count; i++) {
            this.bodyNodes[i].execute(virtualFrame);
        }
        return this.bodyNodes[count].execute(virtualFrame);
    }

    public static PascalRootNode create(FrameSlot[] argumentNames,
            PascalNode[] bodyNodes, FrameDescriptor frameDescriptor) {
        
    	PascalNode[] allNodes = new PascalNode[argumentNames.length + bodyNodes.length];
        for (int i=0; i<argumentNames.length; i++) {
            allNodes[i] = DefineNodeGen.create(
                    new ReadArgumentNode(i), argumentNames[i]);
        }
        System.arraycopy(bodyNodes, 0, allNodes,
                argumentNames.length, bodyNodes.length);
        return new PascalRootNode(allNodes, frameDescriptor);
    }
}
