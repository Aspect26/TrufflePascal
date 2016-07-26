package cz.cuni.mff.d3s.trupple.language.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;

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
