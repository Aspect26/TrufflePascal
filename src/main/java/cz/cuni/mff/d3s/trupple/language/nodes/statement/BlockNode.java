package cz.cuni.mff.d3s.trupple.language.nodes.statement;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * A node representing Pascal's block. It contains list of ordered statements that are executed along this notgd.
 */
@NodeInfo(shortName = "block")
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
