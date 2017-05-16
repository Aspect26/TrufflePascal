package cz.cuni.mff.d3s.trupple.language.nodes.statement;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "block", description = "The node implementation of a block of source code.")
public class BlockNode extends StatementNode {

    @Children
    private final StatementNode[] bodyNodes;

    public BlockNode(StatementNode[] bodyNodes) {
        this.bodyNodes = bodyNodes;
    }

    @Override
    @ExplodeLoop
    public void executeVoid(VirtualFrame virtualFrame) {

        CompilerAsserts.compilationConstant(bodyNodes.length);

        for (StatementNode statement : bodyNodes) {
            statement.executeVoid(virtualFrame);
        }
    }

}
