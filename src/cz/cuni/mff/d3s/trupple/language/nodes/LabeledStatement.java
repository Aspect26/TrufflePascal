package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class LabeledStatement extends StatementNode {

    @Child private StatementNode statement;
    private final String label;

    public LabeledStatement(StatementNode statement, String label) {
        this.statement = statement;
        this.label = label;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        statement.executeVoid(frame);
    }
}
