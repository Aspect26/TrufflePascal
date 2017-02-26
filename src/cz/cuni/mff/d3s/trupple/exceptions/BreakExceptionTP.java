package cz.cuni.mff.d3s.trupple.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class BreakExceptionTP extends ControlFlowException {

	private static final long serialVersionUID = 1461738434684232542L;

	protected BreakExceptionTP() {

	}

	public static BreakExceptionTP SINGLETON = new BreakExceptionTP();
}
