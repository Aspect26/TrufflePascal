package cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;

public class SimpleAccessNode extends AccessNode {

    private final FrameSlot slot;

    public SimpleAccessNode(FrameSlot slot) {
        this.slot = slot;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {

    }

    @Override
    protected void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
        slotAssignment.assign(frame, this.slot, value);
    }

    @Override
    protected void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
        slotAssignment.assign(reference.getFromFrame(), reference.getFrameSlot(), value);
    }

    @Override
    public Object getValue(VirtualFrame frame) throws FrameSlotTypeException {
        return this.getValueFromSlot(frame, this.slot);
    }

}