package cz.cuni.mff.d3s.trupple.language.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.BreakExceptionTP;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;

@NodeInfo(shortName = "while", description = "The node implementing a while loop")
public class WhileNode extends StatementNode {

	@Child
	private ExpressionNode condition;
	@Child
	private StatementNode body;

	public WhileNode(ExpressionNode condition, StatementNode body) {
		this.body = body;
		this.condition = condition;
	}

	@Override
	public void executeVoid(VirtualFrame frame) {
		try {
			while (condition.executeBoolean(frame)) {
				body.executeVoid(frame);
			}
		} catch (BreakExceptionTP e) {
		} catch (UnexpectedResultException e) {
			// TODO HANDLE THIS ERROR
		}
	}
}
