package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;

/**
 * Representation of a subroutine-type variable. In Pascal we may have variables which represent functions or procedures.
 * Our implementation of this type of variable is a slight wrapper to the subroutine's {@link RootCallTarget}.
 */
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
