package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.GotoException;

public class LabeledStatement extends StatementNode {

    @Child private StatementNode statement;
    private final String label;

    public LabeledStatement(StatementNode statement, String label) {
        this.statement = statement;
        this.label = label;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        while (true) {
            try {
                statement.executeVoid(frame);
                break;
            } catch (GotoException e) {
                if (!e.getLabelIdentifier().equals(this.label)) {
                    throw e;
                }
            }
        }
    }
}
