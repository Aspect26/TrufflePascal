package cz.cuni.mff.d3s.trupple.language.nodes.root;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.ProcedureWrapExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.ExitExceptionTP;

/**
 * Node representing the root node of Pascal's main function's AST.
 */
public class MainFunctionPascalRootNode extends PascalRootNode {

    public MainFunctionPascalRootNode(FrameDescriptor frameDescriptor, StatementNode bodyNode) {
        super(frameDescriptor, new ProcedureWrapExpressionNode(bodyNode));
    }

    public Object execute(VirtualFrame virtualFrame) {
        try {
            bodyNode.executeGeneric(virtualFrame);
        } catch (ExitExceptionTP e) {
            return e.getExitCode();
        }

        return 0;
    }

}
