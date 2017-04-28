package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.*;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;

@NodeChild(value = "valueNode", type = ExpressionNode.class)
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AssignmentNode extends StatementNode {

    public interface SlotAssignment {

        void assign(VirtualFrame frame, FrameSlot frameSlot, Object value) throws FrameSlotTypeException;

    }

    protected abstract FrameSlot getSlot();

    protected void makeAssignment(VirtualFrame frame, FrameSlot slot, AssignmentNodeWithRoute.SlotAssignment slotAssignment, Object value) {
        try {
            slotAssignment.assign(frame, slot, value);
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Wrong access");
        }
    }

    @Specialization
    void writeLong(VirtualFrame frame, long value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setLong(assignmentFrameSlot, (long) assignmentValue),
                value
        );
    }

    @Specialization
    void writeBoolean(VirtualFrame frame, boolean value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setBoolean(assignmentFrameSlot, (boolean) assignmentValue),
                value
        );
    }

    // NOTE: characters are stored as bytes, since there is no FrameSlotKind for char
    @Specialization
    void writeChar(VirtualFrame frame, char value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setByte(assignmentFrameSlot, (byte)((char) assignmentValue)),
                value
        );
    }

    @Specialization
    void writeDouble(VirtualFrame frame, double value) {
        this.makeAssignment(
                frame,
                getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot assignmentFrameSlot, Object assignmentValue) -> assignmentFrame.setDouble(assignmentFrameSlot, (double) assignmentValue),
                value
        );
    }

    @Specialization
    void writeEnum(VirtualFrame frame, EnumValue value) {
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, value);
    }

    @Specialization
    void assignSet(VirtualFrame frame, SetTypeValue set) {
        SetTypeValue setCopy = set.createDeepCopy();
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, setCopy);
    }

    @Specialization
    void assignRecord(VirtualFrame frame, RecordValue record) {
        RecordValue recordCopy = record.getCopy();
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, recordCopy);
    }

    @Specialization
    void assignReference(VirtualFrame frame, Reference reference) {
        Object referenceValue = reference.getFromFrame().getValue(reference.getFrameSlot());
        if (referenceValue instanceof PointerValue) {
            this.assignPointers(frame, (PointerValue) referenceValue);
        } else {
            this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, referenceValue);
        }
    }

    @Specialization
    void assignPointers(VirtualFrame frame, PointerValue pointer) {
        this.makeAssignment(frame, getSlot(),
                (VirtualFrame assignmentFrame, FrameSlot frameSlot, Object value) ->
                {
                    PointerValue assignmentTarget = (PointerValue) assignmentFrame.getObject(frameSlot);
                    assignmentTarget.setHeapSlot(((PointerValue) value).getHeapSlot());
                },
                pointer);
    }

    @Specialization
    void assignString(VirtualFrame frame, PascalString value) {
        Object targetObject;
        try {
            targetObject = frame.getObject(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
        if (targetObject instanceof PascalString) {
            this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, value);
        } else if (targetObject instanceof PointerValue) {
            PointerValue pointerValue = (PointerValue) targetObject;
            if (pointerValue.getType() instanceof PCharDesriptor) {
                assignPChar(pointerValue, value);
            }
        }
    }

    @Specialization
    void assignSubroutine(VirtualFrame frame, PascalSubroutine subroutine) {
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, subroutine);
    }

    @Specialization
    void assignArray(VirtualFrame frame, PascalArray array) {
        PascalArray arrayCopy = (PascalArray) array.createDeepCopy();
        this.makeAssignment(frame, getSlot(), VirtualFrame::setObject, arrayCopy);
    }

    private void assignPChar(PointerValue pcharPointer, PascalString value) {
        PCharValue pchar = (PCharValue) pcharPointer.getDereferenceValue();
        pchar.assignString(value.toString());
    }

}
