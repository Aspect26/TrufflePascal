package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * Our {@link cz.cuni.mff.d3s.trupple.language.nodes.root.PascalRootNode} needs to be provided with an {@link ExpressionNode}
 * so we cannot pass it {@link ProcedureBodyNode} directly. We encapsulate the procedure's body inside this node which
 * only executes it and returns null.
 */
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
