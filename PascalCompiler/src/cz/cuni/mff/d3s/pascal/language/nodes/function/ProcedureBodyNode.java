package cz.cuni.mff.d3s.pascal.language.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.pascal.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.pascal.language.nodes.StatementNode;
import cz.cuni.mff.d3s.pascal.language.runtime.Null;

@NodeInfo(shortName = "function body")
public final class ProcedureBodyNode extends ExpressionNode {
	
	/** The body. */
	@Child private StatementNode bodyNode;
	
	public ProcedureBodyNode(StatementNode bodyNode){
		this.bodyNode = bodyNode;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		bodyNode.executeVoid(frame);
		
		return Null.SINGLETON;
	}
}
