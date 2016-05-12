package pascal.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;

import pascal.language.runtime.Null;

@NodeInfo(shortName = "function body")
public final class FunctionBodyNode extends ExpressionNode {
	
	/** The body. */
	@Child private StatementNode bodyNode;
	
	public FunctionBodyNode(StatementNode bodyNode){
		this.bodyNode = bodyNode;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		bodyNode.executeVoid(frame);
		
		//TODO: return function value
		return Null.SINGLETON;
	}
}
