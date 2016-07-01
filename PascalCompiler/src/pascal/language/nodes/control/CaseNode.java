package pascal.language.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.language.nodes.ExpressionNode;
import pascal.language.nodes.StatementNode;

public class CaseNode extends StatementNode {

	@Children private final ExpressionNode[] caseExpressions;
	@Children private final StatementNode[] caseStatements;
	@Child private ExpressionNode caseValue;
	
	public CaseNode(ExpressionNode caseValue, ExpressionNode[] caseExpressions, StatementNode[] caseStatements){
		this.caseExpressions = caseExpressions;
		this.caseStatements = caseStatements;
		this.caseValue = caseValue;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		Object value = caseValue.executeGeneric(frame);
		
		for(int i=0; i<caseExpressions.length; i++){
			if(caseExpressions[i].executeGeneric(frame).equals(value)){
				caseStatements[i].executeVoid(frame);
				return;
			}
		}
		
		//TODO default case option
	}
}
