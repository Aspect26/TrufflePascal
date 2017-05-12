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

    protected void makeAssignment(VirtualFrame frame, FrameSlot slot, SlotAssignment slotAssignment, Object value) {
        try {
            slotAssignment.assign(frame, slot, value);
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Wrong access");
        }
    }

    @Specialization
    void writeLong(VirtualFrame frame, long value) {
        frame.setLong(getSlot(), value);
    }

    @Specialization
    void writeBoolean(VirtualFrame frame, boolean value) {
        frame.setBoolean(getSlot(), value);
    }

    @Specialization
    void writeChar(VirtualFrame frame, char value) {
        frame.setByte(getSlot(), (byte) value);
    }

    @Specialization
    void writeDouble(VirtualFrame frame, double value) {
        frame.setDouble(getSlot(), value);
    }

    @Specialization
    void writeEnum(VirtualFrame frame, EnumValue value) {
        frame.setObject(getSlot(), value);
    }

    @Specialization
    void assignSet(VirtualFrame frame, SetTypeValue set) {
        frame.setObject(getSlot(), set.createDeepCopy());
    }

    @Specialization
    void assignRecord(VirtualFrame frame, RecordValue record) {
        frame.setObject(getSlot(), record.getCopy());
    }

    @Specialization
    void assignReference(VirtualFrame frame, Reference reference) {
        Object referenceValue = reference.getFromFrame().getValue(reference.getFrameSlot());
        if (referenceValue instanceof PointerValue) {
            this.assignPointers(frame, (PointerValue) referenceValue);
        } else {
            // TODO: this does not has to be object...
            frame.setObject(getSlot(), referenceValue);
        }
    }

    @Specialization
    void assignPointers(VirtualFrame frame, PointerValue pointer) {
        PointerValue assignmentTarget = (PointerValue) frame.getValue(getSlot());
        assignmentTarget.setHeapSlot((pointer).getHeapSlot());
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
        frame.setObject(getSlot(), subroutine);
    }

    @Specialization
    void assignArray(VirtualFrame frame, PascalArray array) {
        frame.setObject(getSlot(), array.createDeepCopy());
    }

    void assignPChar(PointerValue pcharPointer, PascalString value) {
        PCharValue pchar = (PCharValue) pcharPointer.getDereferenceValue();
        pchar.assignString(value.toString());
    }

}
