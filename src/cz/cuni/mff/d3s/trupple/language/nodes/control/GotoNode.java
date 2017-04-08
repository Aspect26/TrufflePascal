package cz.cuni.mff.d3s.trupple.language.nodes.control;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;

public class GotoNode extends StatementNode {

    private final String labelIdentifier;

    public GotoNode(String labelIdentifier) {
        this.labelIdentifier = labelIdentifier;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        // TODO: implement in later phases of goto implementation
    }
}
