package cz.cuni.mff.d3s.trupple.language.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public class PascalFunction implements TruffleObject {

	/** The current implementation of this function. */
	private RootCallTarget callTarget;

	private String name;
	private int parametersCount;

	public PascalFunction(String name) {
		this.name = name;
		this.parametersCount = -1;
	}

	protected void setCallTarget(RootCallTarget callTarget) {
		this.callTarget = callTarget;
	}
	
	public void setName(String identifier){
		this.name = identifier;
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

	@Override
	public String toString() {
		return name;
	}

	@Override
	public ForeignAccess getForeignAccess() {
		// TODO Foreign access
		return null;
	}
}
