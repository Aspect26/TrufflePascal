package cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class SimpleAccessNode extends AccessNode {

    private final FrameSlot slot;

    private final TypeDescriptor typeDescriptor;

    public SimpleAccessNode(FrameSlot slot, TypeDescriptor typeDescriptor) {
        super(null);
        this.slot = slot;
        this.typeDescriptor = typeDescriptor;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return this.getValueFromSlot(frame, this.slot);
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
    protected Object applyTo(VirtualFrame frame, Object value) {
        return this.getValueFromSlot(frame, this.slot);
    }

    @Override
    public TypeDescriptor getType() {
        return this.typeDescriptor;
    }

}