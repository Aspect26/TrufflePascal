package cz.cuni.mff.d3s.trupple.language.nodes.root;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.ProcedureWrapExpressionNode;

public class ProcedurePascalRootNode extends PascalRootNode {

    public ProcedurePascalRootNode(FrameDescriptor frameDescriptor, StatementNode bodyNode) {
        super(frameDescriptor, new ProcedureWrapExpressionNode(bodyNode));
    }

}
