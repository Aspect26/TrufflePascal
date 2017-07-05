package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class ExitExceptionTP extends ControlFlowException {

	private static final long serialVersionUID = 146173845468432542L;

	private final int exitCode;

	public ExitExceptionTP(int exitCode) {
        this.exitCode = exitCode;
	}

    public int getExitCode() {
        return exitCode;
    }
}
