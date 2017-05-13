package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import com.oracle.truffle.api.nodes.ExplodeLoop;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.array.PascalArray;
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

    @CompilerDirectives.CompilationFinal private int jumps = -1;

    @Specialization
    void writeLong(VirtualFrame frame, long value) {
        getFrame(frame).setLong(getSlot(), value);
    }

    @Specialization
    void writeBoolean(VirtualFrame frame, boolean value) {
        getFrame(frame).setBoolean(getSlot(), value);
    }

    @Specialization
    void writeChar(VirtualFrame frame, char value) {
        getFrame(frame).setByte(getSlot(), (byte) value);
    }

    @Specialization
    void writeDouble(VirtualFrame frame, double value) {
        getFrame(frame).setDouble(getSlot(), value);
    }

    @Specialization
    void writeEnum(VirtualFrame frame, EnumValue value) {
        getFrame(frame).setObject(getSlot(), value);
    }

    @Specialization
    void assignSet(VirtualFrame frame, SetTypeValue set) {
        getFrame(frame).setObject(getSlot(), set.createDeepCopy());
    }

    @Specialization
    void assignRecord(VirtualFrame frame, RecordValue record) {
        getFrame(frame).setObject(getSlot(), record.getCopy());
    }

    @Specialization
    void assignPointers(VirtualFrame frame, PointerValue pointer) {
        PointerValue assignmentTarget = (PointerValue) getFrame(frame).getValue(getSlot());
        assignmentTarget.setHeapSlot((pointer).getHeapSlot());
    }

    @Specialization
    void assignString(VirtualFrame frame, PascalString value) {
        frame = getFrame(frame);
        Object targetObject = frame.getValue(getSlot());
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
        getFrame(frame).setObject(getSlot(), subroutine);
    }

    @Specialization
    void assignArray(VirtualFrame frame, PascalArray array) {
        getFrame(frame).setObject(getSlot(), array.createDeepCopy());
    }

    void assignPChar(PointerValue pcharPointer, PascalString value) {
        PCharValue pchar = (PCharValue) pcharPointer.getDereferenceValue();
        pchar.assignString(value.toString());
    }

    @ExplodeLoop
    protected VirtualFrame getFrame(VirtualFrame frame) {
        if (jumps == -1) {
            jumps = this.getJumpsToFrame(frame, getSlot());
            CompilerDirectives.transferToInterpreterAndInvalidate();
        }

        for (int i = 0; i < jumps; ++i) {
            frame = (VirtualFrame) frame.getArguments()[0];
        }

        return frame;
    }

}
