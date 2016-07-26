package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import cz.cuni.mff.d3s.trupple.language.PascalTypes;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;

@TypeSystemReference(PascalTypes.class)
@NodeInfo(description = "Abstract class for all nodes that return value")
public abstract class ExpressionNode extends StatementNode {

	public abstract Object executeGeneric(VirtualFrame frame);
	
	@Override
	public void executeVoid(VirtualFrame virtualFrame) {
		executeGeneric(virtualFrame);
	}
	
	public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException{
		return PascalTypesGen.expectBoolean(executeGeneric(frame));
	}
	
	public long executeLong(VirtualFrame frame) throws UnexpectedResultException{
		return PascalTypesGen.expectLong(executeGeneric(frame));
	}
	
	public char executeChar(VirtualFrame frame) throws UnexpectedResultException{
		return PascalTypesGen.expectCharacter(executeGeneric(frame));
	}
}
