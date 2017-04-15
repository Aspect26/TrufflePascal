package cz.cuni.mff.d3s.trupple.language.runtime.exceptions;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class GotoException extends ControlFlowException {

    private final String labelIdentifier;

    public GotoException(String labelIdentifier) {
        this.labelIdentifier = labelIdentifier;
    }

    public String getLabelIdentifier() {
        return labelIdentifier;
    }
}
