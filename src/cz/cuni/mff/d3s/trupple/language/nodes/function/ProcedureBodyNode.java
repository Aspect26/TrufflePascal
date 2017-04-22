package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

@NodeInfo(shortName = "procedure body")
public final class ProcedureBodyNode extends StatementNode {

	@Child
	private StatementNode bodyNode;

	public ProcedureBodyNode(StatementNode bodyNode) {
		this.bodyNode = bodyNode;
	}

	@Override
	public void executeVoid(VirtualFrame frame) {
		bodyNode.executeVoid(frame);
	}
}
