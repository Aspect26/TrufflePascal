package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

/**
 * This exception is thrown when a Pascal's break statement is executed. It is caught inside each loop node. Catching
 * this exception ends the loop.
 */
public class BreakExceptionTP extends ControlFlowException {

	private BreakExceptionTP() {

	}

	public static BreakExceptionTP SINGLETON = new BreakExceptionTP();
}
