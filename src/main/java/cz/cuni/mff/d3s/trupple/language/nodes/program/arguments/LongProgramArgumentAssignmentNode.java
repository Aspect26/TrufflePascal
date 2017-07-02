package cz.cuni.mff.d3s.trupple.language.nodes.program.arguments;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

public class LongProgramArgumentAssignmentNode extends StatementNode {

    private final FrameSlot frameSlot;
    private final int index;

    public LongProgramArgumentAssignmentNode(FrameSlot frameSlot, int index) {
        this.frameSlot = frameSlot;
        this.index = index;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        frame.setLong(this.frameSlot, (Long) frame.getArguments()[this.index]);
    }

}
