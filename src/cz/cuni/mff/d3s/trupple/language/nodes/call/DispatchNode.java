package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

import cz.cuni.mff.d3s.trupple.language.runtime.PascalSubroutine;

public abstract class DispatchNode extends Node {

	public abstract Object executeDispatch(VirtualFrame frame, PascalSubroutine function, Object[] arguments);

	@Specialization(guards = "function.getCallTarget() == null")
	protected Object doundefinedFunction(PascalSubroutine function, Object[] arguments) {
		throw new RuntimeException("Undefined function: " + function.toString() + "!");
	}

	@Specialization
	protected static Object doDirect(PascalSubroutine subroutine, Object[] arguments) {
		return subroutine.getCallTarget().call(arguments);
	}
}
