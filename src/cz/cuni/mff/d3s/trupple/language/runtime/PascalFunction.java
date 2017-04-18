package cz.cuni.mff.d3s.trupple.language.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public class PascalFunction implements TruffleObject {

	private RootCallTarget callTarget;

	public PascalFunction(RootCallTarget rootCallTarget) {
		this.callTarget = rootCallTarget;
	}

	public PascalFunction() {
		this(null);
	}

	void setCallTarget(RootCallTarget callTarget) {
		this.callTarget = callTarget;
	}
	
	public RootCallTarget getCallTarget() {
		return callTarget;
	}
	
	boolean isImplemented() {
		return callTarget != null;
	}
	
	static PascalFunction createUnimplementedFunction() {
        return new PascalFunction();
    }

	@Override
	public ForeignAccess getForeignAccess() {
		return null;
	}
}
