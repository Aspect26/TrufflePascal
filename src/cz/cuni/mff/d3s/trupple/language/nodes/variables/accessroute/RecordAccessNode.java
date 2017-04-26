package cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class RecordAccessNode extends AccessNode {

    private final String variableIdentifier;

    private final TypeDescriptor typeDescriptor;

    public RecordAccessNode(AccessNode applyToNode, String variableIdentifier, TypeDescriptor typeDescriptor) {
        super(applyToNode);
        this.variableIdentifier = variableIdentifier;
        this.typeDescriptor = typeDescriptor;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        this.applyToNode.executeVoid(frame);
        return null;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        this.applyToNode.executeVoid(frame);
    }

    @Override
    public TypeDescriptor getType() {
        return this.typeDescriptor;
    }

    @Override
    protected void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
        RecordValue record = (RecordValue) this.applyToNode.getValue(frame);
        VirtualFrame recordsFrame = record.getFrame();
        FrameSlot targetSlot = this.findSlotByIdentifier(recordsFrame, this.variableIdentifier);
        slotAssignment.assign(recordsFrame, targetSlot, value);
    }

    @Override
    protected void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) {
        throw new PascalRuntimeException("Record element cannot be a reference.");
    }

    @Override
    protected Object applyTo(VirtualFrame frame, Object value) {
        if (value instanceof RecordValue) {
            RecordValue record = (RecordValue) value;
            VirtualFrame recordsFrame = record.getFrame();
            FrameSlot targetSlot = this.findSlotByIdentifier(recordsFrame, this.variableIdentifier);

            return this.getValueFromSlot(recordsFrame, targetSlot);
        } else {
            throw new PascalRuntimeException("Can't access element of this type.");
        }
    }

}