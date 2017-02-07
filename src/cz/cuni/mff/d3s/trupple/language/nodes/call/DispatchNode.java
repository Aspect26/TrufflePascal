package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunction;

public abstract class DispatchNode extends Node {

	public abstract Object executeDispatch(VirtualFrame frame, PascalFunction function, Object[] arguments);

	@Specialization(guards = "function.getCallTarget() == null")
	protected Object doundefinedFunction(PascalFunction function, Object[] arguments) {
		throw new RuntimeException("Undefined function: " + function.toString() + "!");
	}

	@Specialization
	protected static Object doDirect(VirtualFrame frame, PascalFunction function, Object[] arguments) {
		return function.getCallTarget().call(arguments);
	}
}
