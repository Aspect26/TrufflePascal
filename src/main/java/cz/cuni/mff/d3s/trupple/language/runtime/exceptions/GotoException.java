package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

/**
 * Exception is thrown when Pascal's goto statement is executed. It is caught inside a {@link cz.cuni.mff.d3s.trupple.language.nodes.statement.LabeledStatement}
 * or {@link cz.cuni.mff.d3s.trupple.language.nodes.statement.ExtendedBlockNode}.
 */
public class GotoException extends ControlFlowException {

    private final String labelIdentifier;

    public GotoException(String labelIdentifier) {
        this.labelIdentifier = labelIdentifier;
    }

    public String getLabelIdentifier() {
        return labelIdentifier;
    }
}
