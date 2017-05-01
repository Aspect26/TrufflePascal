package cz.cuni.mff.d3s.trupple.language.nodes.root;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.ProcedureWrapExpressionNode;

/**
 * This node represents a root node for each Pascal procedure. Since procedures do not return any value
 * they are represented by a statement node. This statement node therefore needs to be wrapped into an
 * expression node to be passed to the parent in c'tor.
 */
public class ProcedurePascalRootNode extends PascalRootNode {

    public ProcedurePascalRootNode(FrameDescriptor frameDescriptor, StatementNode bodyNode) {
        super(frameDescriptor, new ProcedureWrapExpressionNode(bodyNode));
    }

}
