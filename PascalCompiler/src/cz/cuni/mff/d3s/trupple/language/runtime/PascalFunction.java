package cz.cuni.mff.d3s.trupple.language.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public class PascalFunction implements TruffleObject {

	private RootCallTarget callTarget;
	private String identifier;
	private int parametersCount;
	private boolean isImplemented;

	public PascalFunction(String name) {
		this.identifier = name;
		this.parametersCount = -1;
		this.isImplemented = false;
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
		return isImplemented;
	}
	
	public void setImplemented(boolean isImplemented) {
		this.isImplemented = isImplemented;
	}

	@Override
	public String toString() {
		return identifier;
	}

	@Override
	public ForeignAccess getForeignAccess() {
		// TODO Foreign access
		return null;
	}
}
