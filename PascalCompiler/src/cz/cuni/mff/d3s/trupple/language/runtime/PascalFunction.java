package cz.cuni.mff.d3s.trupple.language.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;

public class PascalFunction implements TruffleObject {

	private RootCallTarget callTarget;
	private String identifier;
	private int parametersCount;

	public PascalFunction(String identifier) {
		this.identifier = identifier;
		this.parametersCount = -1;
	}

	protected void setCallTarget(RootCallTarget callTarget) {
		this.callTarget = callTarget;
	}
	
	public void setName(String identifier){
		this.identifier = identifier;
	}
	
	public void setParametersCount(int count){
		this.parametersCount = count;
	}

	public int getParametersCount(){
		return this.parametersCount;
	}
	
	public RootCallTarget getCallTarget() {
		return callTarget;
	}
	
	public boolean isImplemented() {
		return callTarget != null;
	}
	
	@Override
	public String toString() {
		return "Function: " + identifier;
	}

	@Override
	public ForeignAccess getForeignAccess() {
		// TODO Foreign access
		return null;
	}

	public static PascalFunction createUnimplementedFunctin() {
        return new PascalFunction("_UnimplementedFunction");
    }
}
