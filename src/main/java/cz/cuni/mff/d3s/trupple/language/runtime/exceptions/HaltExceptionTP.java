package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

/**
 * Exception thrown when Pascal's halt function is executed ({@link cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp.HaltBuiltinNode}).
 * The exception is caught in main block's root node {@link cz.cuni.mff.d3s.trupple.language.nodes.root.MainFunctionPascalRootNode}.
 */
public class HaltExceptionTP extends ControlFlowException {

	private static final long serialVersionUID = 146173845468432542L;

	private final int exitCode;

	public HaltExceptionTP(int exitCode) {
        this.exitCode = exitCode;
	}

    public int getExitCode() {
        return exitCode;
    }

}
