package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class BreakExceptionTP extends ControlFlowException {

	private static final long serialVersionUID = 1461738434684232542L;

	private BreakExceptionTP() {

	}

	public static BreakExceptionTP SINGLETON = new BreakExceptionTP();
}
