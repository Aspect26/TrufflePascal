package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;

import java.util.List;

public class WithNode extends StatementNode {

    private final List<FrameSlot> recordSlots;

    @Child private StatementNode innerStatement;

    public WithNode(List<FrameSlot> recordSlots, StatementNode innerStatement) {
        this.recordSlots = recordSlots;
        this.innerStatement = innerStatement;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        try {
            for (FrameSlot recordSlot : this.recordSlots) {
                RecordValue record = (RecordValue) frame.getObject(recordSlot);
                frame = record.getFrame();
            }

            innerStatement.executeVoid(frame);
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected accessing of non record type");
        }
    }
}
