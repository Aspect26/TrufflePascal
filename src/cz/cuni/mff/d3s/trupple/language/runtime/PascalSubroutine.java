package cz.cuni.mff.d3s.trupple.language.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public class PascalSubroutine implements TruffleObject {

	private RootCallTarget callTarget;

	public PascalSubroutine(RootCallTarget rootCallTarget) {
		this.callTarget = rootCallTarget;
	}

	public PascalSubroutine() {
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
	
	static PascalSubroutine createUnimplementedFunction() {
        return new PascalSubroutine();
    }

	@Override
	public ForeignAccess getForeignAccess() {
		return null;
	}
}
