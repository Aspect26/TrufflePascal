package cz.cuni.mff.d3s.trupple.language.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.GotoException;

public class GotoNode extends StatementNode {

    private final String labelIdentifier;

    public GotoNode(String labelIdentifier) {
        this.labelIdentifier = labelIdentifier;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        throw new GotoException(this.labelIdentifier);
    }
}
