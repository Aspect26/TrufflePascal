package pascal.language.nodes;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.PascalTypes;

@TypeSystemReference(PascalTypes.class)
@NodeInfo(description = "Abstract class for all nodes that return value")
public abstract class ExpressionNode extends StatementNode {

	public abstract Object executeGeneric(VirtualFrame frame);
	
	@Override
	public void executeVoid(VirtualFrame virtualFrame) {
		executeGeneric(virtualFrame);
	}
}
