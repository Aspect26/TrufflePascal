package pascal.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import pascal.language.runtime.PascalFunction;

@NodeInfo(language = "Pascal", description = "The abstract base class for all Pascal nodes")
public abstract class PascalNode extends Node{
	
	public abstract Object execute(VirtualFrame virtualFrame);
	
	public int executeInteger(VirtualFrame virtualFrame) throws UnexpectedResultException{
		return PascalTypesGen.expectInteger(this.execute(virtualFrame));
	}
	
	public long executeLong(VirtualFrame virtualFrame) throws UnexpectedResultException{
		return PascalTypesGen.expectLong(this.execute(virtualFrame));
	}
	
	public boolean executeBoolean(VirtualFrame virtualFrame) throws UnexpectedResultException{
		return PascalTypesGen.expectBoolean(this.execute(virtualFrame));
	}
	
	public PascalFunction executeFunction(VirtualFrame virtualFrame) throws UnexpectedResultException{
		return PascalTypesGen.expectPascalFunction(this.execute(virtualFrame));
	}
	
	protected boolean isArgumentIndexInRange(VirtualFrame virtualFrame, int index) {
        return (index + 1) < virtualFrame.getArguments().length;
    }
	
	protected Object getArgument(VirtualFrame virtualFrame, int index) {
        return virtualFrame.getArguments()[index + 1];
    }
}
