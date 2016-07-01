package pascal.language.nodes.control;

import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import pascal.language.nodes.ExpressionNode;
import pascal.language.nodes.StatementNode;

@NodeInfo(shortName = "if", description = "The node implementing a conditional statement")
public final class IfNode extends StatementNode{

	@Child ExpressionNode conditionNode;
	@Child StatementNode thenNode;
	@Child StatementNode elseNode;
	
	public IfNode(ExpressionNode conditionNode, StatementNode thenNode, StatementNode elseNode){
		this.conditionNode = conditionNode;
		this.thenNode = thenNode;
		this.elseNode = elseNode;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		if(checkCondition(frame)){
			thenNode.executeVoid(frame);
		} else {
			if(elseNode != null){
				elseNode.executeVoid(frame);
			}
		}
	}
	
	private boolean checkCondition(VirtualFrame frame){
		try{
			return conditionNode.executeBoolean(frame);
		} catch(UnexpectedResultException e){
			throw new UnsupportedSpecializationException(this, new Node[]{conditionNode}, e.getResult());
		}
	}
}
