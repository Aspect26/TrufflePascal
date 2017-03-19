package cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;

public class RecordAccessNode extends AccessNode {

    @Child AccessNode previousNode;
    private final String variableIdentifier;

    public RecordAccessNode(AccessNode previousNode, String variableIdentifier) {
        this.previousNode = previousNode;
        this.variableIdentifier = variableIdentifier;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        previousNode.executeVoid(frame);
    }

    @Override
    protected void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
        RecordValue record = (RecordValue) this.previousNode.getValue(frame);
        VirtualFrame recordsFrame = record.getFrame();
        FrameSlot targetSlot = this.findSlotByIdentifier(recordsFrame, this.variableIdentifier);
        slotAssignment.assign(recordsFrame, targetSlot, value);
    }

    @Override
    protected void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) {
        throw new PascalRuntimeException("Record element cannot be a reference.");
    }

    @Override
    public Object getValue(VirtualFrame frame) throws FrameSlotTypeException {
        RecordValue record = (RecordValue) this.previousNode.getValue(frame);
        VirtualFrame recordsFrame = record.getFrame();
        FrameSlot targetSlot = this.findSlotByIdentifier(recordsFrame, this.variableIdentifier);

        return this.getValueFromSlot(recordsFrame, targetSlot);
    }

}