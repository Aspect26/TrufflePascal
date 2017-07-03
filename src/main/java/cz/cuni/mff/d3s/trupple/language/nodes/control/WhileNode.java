package cz.cuni.mff.d3s.trupple.language.nodes.control;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.*;

import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.api.profiles.LoopConditionProfile;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.BreakExceptionTP;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;

/**
 * Node representing Pascal's while loop.
 */
@NodeInfo(shortName = "while", description = "The node implementing a while loop")
public class WhileNode extends StatementNode {

    private static class WhileRepeatingNode extends Node implements RepeatingNode {

        @Child
        private ExpressionNode condition;
        @Child
        private StatementNode body;

        private final LoopConditionProfile conditionProfile = LoopConditionProfile.createCountingProfile();

        private WhileRepeatingNode(ExpressionNode condition, StatementNode body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        public boolean executeRepeating(VirtualFrame frame) {
            try {
                if (conditionProfile.profile(condition.executeBoolean(frame))) {
                    try {
                        body.executeVoid(frame);
                    } catch (BreakExceptionTP e) {
                        return false;
                    }
                    return true;
                } else {
                    return false;
                }
            } catch (UnexpectedResultException e) {
                // This should not happen thanks to our compile time type checking
                throw new PascalRuntimeException("Condition node provided to while is not boolean type");
            }
        }
    }

	@Child
    private LoopNode loopNode;

	public WhileNode(ExpressionNode condition, StatementNode body) {
	    this.loopNode = Truffle.getRuntime().createLoopNode(new WhileRepeatingNode(condition, body));
	}

	@Override
	public void executeVoid(VirtualFrame frame) {
		this.loopNode.executeLoop(frame);
	}
}
