package cz.cuni.mff.d3s.trupple.language.nodes.variables.write;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.*;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;

@NodeChild(value = "valueNode", type = ExpressionNode.class)
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AssignReferenceNode extends StatementNode {

    protected abstract FrameSlot getSlot();

    @CompilerDirectives.CompilationFinal private int jumps = -1;

    @Specialization
    void writeInt(VirtualFrame frame, int value) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        reference.getFromFrame().setInt(reference.getFrameSlot(), value);
    }

    @Specialization
    void writeLong(VirtualFrame frame, long value) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        reference.getFromFrame().setLong(reference.getFrameSlot(), value);
    }

    @Specialization
    void writeBoolean(VirtualFrame frame, boolean value) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        reference.getFromFrame().setBoolean(reference.getFrameSlot(), value);
    }

    @Specialization
    void writeChar(VirtualFrame frame, char value) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        reference.getFromFrame().setByte(reference.getFrameSlot(), (byte) value);
    }

    @Specialization
    void writeDouble(VirtualFrame frame, double value) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        reference.getFromFrame().setDouble(reference.getFrameSlot(), value);
    }

    @Specialization
    void writeEnum(VirtualFrame frame, EnumValue value) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        reference.getFromFrame().setObject(reference.getFrameSlot(), value);
    }

    @Specialization
    void assignSet(VirtualFrame frame, SetTypeValue set) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        reference.getFromFrame().setObject(reference.getFrameSlot(), set.createDeepCopy());
    }

    @Specialization
    void assignRecord(VirtualFrame frame, RecordValue record) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        reference.getFromFrame().setObject(reference.getFrameSlot(), record.getCopy());
    }

    @Specialization
    void assignPointer(VirtualFrame frame, PointerValue pointer) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        PointerValue assignmentTarget = (PointerValue) reference.getFromFrame().getValue(reference.getFrameSlot());
        assignmentTarget.setHeapSlot(pointer.getHeapSlot());
    }

    @Specialization
    void assignSubroutine(VirtualFrame frame, PascalSubroutine subroutine) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        reference.getFromFrame().setObject(reference.getFrameSlot(), subroutine);
    }

    @Specialization
    void assignString(VirtualFrame frame, PascalString value) {
        frame = getFrame(frame);
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        Object targetObject = reference.getFromFrame().getValue(reference.getFrameSlot());
        if (targetObject instanceof PascalString) {
            reference.getFromFrame().setObject(reference.getFrameSlot(), value);
        } else if (targetObject instanceof PointerValue) {
            PointerValue pointerValue = (PointerValue) targetObject;
            if (pointerValue.getType() instanceof PCharDesriptor) {
                assignPChar(pointerValue, value);
            }
        }
    }

    @Specialization
    void assignArray(VirtualFrame frame, PascalArray array) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
        reference.getFromFrame().setObject(reference.getFrameSlot(), array.createDeepCopy());
    }

    private void assignPChar(PointerValue pcharPointer, PascalString value) {
        PCharValue pchar = (PCharValue) pcharPointer.getDereferenceValue();
        pchar.assignString(value.toString());
    }

    @ExplodeLoop
    private VirtualFrame getFrame(VirtualFrame frame) {
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
