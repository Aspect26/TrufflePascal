package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

public class ReadFromRecordNode extends ExpressionNode {

    private final FrameSlot recordSlot;
    @Child private ExpressionNode innerReadNode;

    public ReadFromRecordNode(FrameSlot recordSlot, ExpressionNode innerReadNode) {
        this.recordSlot = recordSlot;
        this.innerReadNode = innerReadNode;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        try {
            RecordValue record = (RecordValue) frame.getObject(this.recordSlot);
            return this.innerReadNode.executeGeneric(record.getFrame());
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Not a record");
        }
    }
}
