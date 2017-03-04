package cz.cuni.mff.d3s.trupple.language.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public class PascalFunction implements TruffleObject {

	private RootCallTarget callTarget;
	private String identifier;

	public PascalFunction(String identifier, RootCallTarget rootCallTarget) {
		this.identifier = identifier;
		this.callTarget = rootCallTarget;
	}

	public PascalFunction(String identifier) {
		this(identifier, null);
	}

	void setCallTarget(RootCallTarget callTarget) {
		this.callTarget = callTarget;
	}
	
	public void setName(String identifier){
		this.identifier = identifier;
	}
	
	public RootCallTarget getCallTarget() {
		return callTarget;
	}
	
	boolean isImplemented() {
		return callTarget != null;
	}
	
	@Override
	public String toString() {
		return "Function: " + identifier;
	}

	static PascalFunction createUnimplementedFunction() {
        return new PascalFunction("_UnimplementedFunction");
    }

	@Override
	public ForeignAccess getForeignAccess() {
		return null;
	}
}
