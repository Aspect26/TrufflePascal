package pascal.language.nodes.builtin;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.language.nodes.PascalNode;
import pascal.language.nodes.PascalRootNode;
import pascal.language.nodes.ReadArgumentNode;
import pascal.language.runtime.PascalContext;
import pascal.language.runtime.PascalFunction;

@NodeChild(value = "arguments", type = PascalNode[].class)
@NodeField(name = "context", type = PascalContext.class)
public abstract class BuiltinNode extends PascalNode {
	
	public abstract PascalContext getContext();
	
	public static PascalFunction createBuiltinFunction(
            NodeFactory<? extends BuiltinNode> factory,
            VirtualFrame outerFrame) {
		
        int argumentCount = factory.getExecutionSignature().size();
        PascalNode[] argumentNodes = new PascalNode[argumentCount];
        for (int i=0; i<argumentCount; i++) {
            argumentNodes[i] = new ReadArgumentNode(i);
        }
        BuiltinNode node = factory.createNode((Object) argumentNodes);
        return new PascalFunction(Truffle.getRuntime().createCallTarget(
                new PascalRootNode(new PascalNode[] {node},
                        new FrameDescriptor())));
    }
}
