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
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.*;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;

import java.util.Arrays;

@NodeField(name = "slot", type = FrameSlot.class)
@NodeChild(value = "valueNode", type = ExpressionNode.class)
public abstract class SimpleAssignmentNode extends StatementNode {

    protected abstract FrameSlot getSlot();

    @CompilerDirectives.CompilationFinal private int jumps = -1;

    @Specialization
    void writeInt(VirtualFrame frame, int value) {
        getFrame(frame).setInt(getSlot(), value);
    }

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
            frame.setObject(getSlot(), value);
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
    void assignIntArray(VirtualFrame frame, int[] array) {
        getFrame(frame).setObject(getSlot(), Arrays.copyOf(array, array.length));
    }

    @Specialization
    void assignLongArray(VirtualFrame frame, long[] array) {
        getFrame(frame).setObject(getSlot(), Arrays.copyOf(array, array.length));
    }

    @Specialization
    void assignDoubleArray(VirtualFrame frame, double[] array) {
        getFrame(frame).setObject(getSlot(), Arrays.copyOf(array, array.length));
    }

    @Specialization
    void assignCharArray(VirtualFrame frame, char[] array) {
        getFrame(frame).setObject(getSlot(), Arrays.copyOf(array, array.length));
    }

    @Specialization
    void assignBooleanArray(VirtualFrame frame, boolean[] array) {
        getFrame(frame).setObject(getSlot(), Arrays.copyOf(array, array.length));
    }

    @Specialization
    void assignArray(VirtualFrame frame, PascalArray array) {
        getFrame(frame).setObject(getSlot(), array.createDeepCopy());
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
