package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;

@CompilerDirectives.ValueType
public class PascalSubroutine {

	private RootCallTarget callTarget;

	public PascalSubroutine(RootCallTarget rootCallTarget) {
		this.callTarget = rootCallTarget;
	}

	public PascalSubroutine() {
		this(null);
	}

	public RootCallTarget getCallTarget() {
		return callTarget;
	}
	
}
