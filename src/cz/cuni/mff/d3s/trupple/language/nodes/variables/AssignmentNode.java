package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AssignmentNode extends ExpressionNode {

    protected interface SlotAssignment {

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
        PascalArray arrayCopy = array.createDeepCopy();
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

}
