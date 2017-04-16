package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.*;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AssignmentNode extends ExpressionNode {

    public interface SlotAssignment {

        void assign(VirtualFrame frame, FrameSlot frameSlot, Object value) throws FrameSlotTypeException;

    }

    protected abstract FrameSlot getSlot();

    // TODO: the third argument shall be removed
    protected void makeAssignment(VirtualFrame frame, FrameSlot slot, AssignmentNodeWithRoute.SlotAssignment slotAssignment, Object value) {
        try {
            this.setValueToSlot(frame, slot, value);
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Wrong access");
        }
    }

    @Specialization
    long writeLong(VirtualFrame frame, long value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setLong(assignmentFrameSlot, (long) assignmentValue),
                value
        );

        return value;
    }

    @Specialization
    boolean writeBoolean(VirtualFrame frame, boolean value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setBoolean(assignmentFrameSlot, (boolean) assignmentValue),
                value
        );

        return value;
    }

    // NOTE: characters are stored as bytes, since there is no FrameSlotKind for char
    @Specialization
    char writeChar(VirtualFrame frame, char value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setByte(assignmentFrameSlot, (byte)((char) assignmentValue)),
                value
        );

        return value;
    }

    @Specialization
    double writeDouble(VirtualFrame frame, double value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setDouble(assignmentFrameSlot, (double) assignmentValue),
                value
        );

        return value;
    }

    @Specialization
    Object writeEnum(VirtualFrame frame, EnumValue value) {
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, value);
        return value;
    }

    @Specialization
    Object assignArray(VirtualFrame frame, PascalArray array) {
        PascalArray arrayCopy = (PascalArray) array.createDeepCopy();
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, arrayCopy);
        return arrayCopy;
    }

    @Specialization
    Object assignSet(VirtualFrame frame, SetTypeValue set) {
        SetTypeValue setCopy = set.createDeepCopy();
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, setCopy);
        return setCopy;
    }

    @Specialization
    Object assignRecord(VirtualFrame frame, RecordValue record) {
        RecordValue recordCopy = record.getCopy();
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, recordCopy);
        return recordCopy;
    }

    @Specialization
    Object assignReference(VirtualFrame frame, Reference reference) {
        // TODO: this if is so wrong, refactor it somehow
        Object referenceValue = this.getValueFromSlot(reference.getFromFrame(), reference.getFrameSlot());
        if (referenceValue instanceof PointerValue) {
            this.assignPointers(frame, (PointerValue) referenceValue);
        } else {
            this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, referenceValue);
        }
        return referenceValue;
    }

    @Specialization
    Object assignPointers(VirtualFrame frame, PointerValue pointer) {
        this.makeAssignment(frame, getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot frameSlot, Object value) ->
                {
                    PointerValue assignmentTarget = (PointerValue) assignmentFrame.getObject(frameSlot);
                    assignmentTarget.setHeapSlot(((PointerValue) value).getHeapSlot());
                },
                pointer);
        return pointer;
    }

    @Specialization
    String assignString(VirtualFrame frame, String value) {
        Object targetObject;
        try {
            targetObject = frame.getObject(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
        if (targetObject instanceof String) {
            this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, value);
        } else if (targetObject instanceof PointerValue) {
            PointerValue pointerValue = (PointerValue) targetObject;
            if (pointerValue.getType() instanceof PCharDesriptor) {
                assignPChar(pointerValue, value);
            }
        }

        return value;
    }

    private void assignPChar(PointerValue pcharPointer, String value) {
        PCharValue pchar = (PCharValue) pcharPointer.getDereferenceValue();
        pchar.assignString(value);
    }

}
