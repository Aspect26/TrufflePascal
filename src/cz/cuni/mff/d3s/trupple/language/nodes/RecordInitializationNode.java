package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.List;

public class RecordInitializationNode extends StatementNode {

    private final VirtualFrame recordFrame;
    private final List<StatementNode> initializationNodes;

    public RecordInitializationNode(VirtualFrame recordFrame, List<StatementNode> initializationNodes) {
        this.recordFrame = recordFrame;
        this.initializationNodes = initializationNodes;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        for (StatementNode initializationNode : this.initializationNodes) {
            initializationNode.executeVoid(this.recordFrame);
        }
    }
}