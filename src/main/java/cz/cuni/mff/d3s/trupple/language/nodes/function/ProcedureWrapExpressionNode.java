package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class ProcedureWrapExpressionNode extends ExpressionNode {

    @Child
    private StatementNode procedureNode;

    public ProcedureWrapExpressionNode(StatementNode procedureNode) {
        this.procedureNode = procedureNode;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        procedureNode.executeVoid(frame);
        return null;
    }

    @Override
    public TypeDescriptor getType() {
        return null;
    }
}
