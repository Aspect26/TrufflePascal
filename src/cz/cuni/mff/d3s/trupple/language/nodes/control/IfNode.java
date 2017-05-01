package cz.cuni.mff.d3s.trupple.language.nodes.control;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import com.oracle.truffle.api.profiles.ConditionProfile;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;

@NodeInfo(shortName = "if", description = "The node implementing a conditional statement")
public final class IfNode extends StatementNode {

	@Child
	private ExpressionNode conditionNode;
	@Child
	private StatementNode thenNode;
	@Child
	private StatementNode elseNode;
    private final ConditionProfile conditionProfile = ConditionProfile.createCountingProfile();
    private final boolean containsElseNode;

	public IfNode(ExpressionNode conditionNode, StatementNode thenNode, StatementNode elseNode) {
		this.conditionNode = conditionNode;
		this.thenNode = thenNode;
		this.elseNode = elseNode;
        containsElseNode = elseNode != null;
	}

	@Override
	public void executeVoid(VirtualFrame frame) {
		if (conditionProfile.profile(checkCondition(frame))) {
			thenNode.executeVoid(frame);
		} else {
			if (containsElseNode) {
				elseNode.executeVoid(frame);
			}
		}
	}

	private boolean checkCondition(VirtualFrame frame) {
		try {
			return conditionNode.executeBoolean(frame);
		} catch (UnexpectedResultException e) {
		    // This should not happen thanks to our compile time type checking
			throw new PascalRuntimeException("The condition node provided for if is not boolean type");
		}
	}
}
