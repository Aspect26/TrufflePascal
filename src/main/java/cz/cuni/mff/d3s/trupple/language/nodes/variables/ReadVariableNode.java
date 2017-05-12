package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import com.oracle.truffle.api.nodes.ExplodeLoop;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.UnexpectedRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.BooleanConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.CharConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.LongConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.RealConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeFields({
    @NodeField(name = "slot", type = FrameSlot.class),
    @NodeField(name = "typeDescriptor", type = TypeDescriptor.class),
    @NodeField(name = "isReference", type = Boolean.class)
})
public abstract class ReadVariableNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();

	protected abstract TypeDescriptor getTypeDescriptor();

	protected abstract Boolean getIsReference();

	@CompilerDirectives.CompilationFinal private int jumps = -1;

	@Specialization(guards = "isLong()")
    long readLong(VirtualFrame frame) {
        try {
            return getFrame(frame).getLong(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isDouble()")
    double readDouble(VirtualFrame frame) {
        try {
            return getFrame(frame).getDouble(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isChar()")
    char readChar(VirtualFrame frame) {
        try {
            return (char) getFrame(frame).getByte(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isBoolean()")
    boolean readBoolean(VirtualFrame frame) {
        try {
            return getFrame(frame).getBoolean(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isLongReference()")
    long readLongReference(VirtualFrame frame) {
        try {
            Reference reference = (Reference) getFrame(frame).getValue(getSlot());
            return reference.getFromFrame().getLong(reference.getFrameSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isDoubleReference()")
    double readDoubleReference(VirtualFrame frame) {
        try {
            Reference reference = (Reference) getFrame(frame).getValue(getSlot());
            return reference.getFromFrame().getDouble(reference.getFrameSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isCharReference()")
    char readCharReference(VirtualFrame frame) {
        try {
            Reference reference = (Reference) getFrame(frame).getValue(getSlot());
            return (char) reference.getFromFrame().getByte(reference.getFrameSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isBooleanReference()")
    boolean readBooleanReference(VirtualFrame frame) {
        try {
            Reference reference = (Reference) getFrame(frame).getValue(getSlot());
            return reference.getFromFrame().getBoolean(reference.getFrameSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isReference()")
    Object readReference(VirtualFrame frame) {
        Reference reference = (Reference) getFrame(frame).getValue(getSlot());
	    return reference.getFromFrame().getValue(reference.getFrameSlot());
    }

    @Specialization
    Object readGeneric(VirtualFrame frame) {
	    return getFrame(frame).getValue(getSlot());
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

    boolean isLong() {
	    return getType() == LongDescriptor.getInstance() || getType() instanceof LongConstantDescriptor;
    }

    boolean isDouble() {
	    return getType() == RealDescriptor.getInstance() || getType() instanceof RealConstantDescriptor;
    }

    boolean isChar() {
	    return getType() == CharDescriptor.getInstance() || getType() instanceof CharConstantDescriptor;
    }

    boolean isBoolean() {
	    return getType() == BooleanDescriptor.getInstance() || getType() instanceof BooleanConstantDescriptor;
    }

    boolean isLongReference() {
	    return this.isLong() && getIsReference();
    }

    boolean isDoubleReference() {
	    return this.isDouble() && getIsReference();
    }

    boolean isCharReference() {
	    return this.isChar() && getIsReference();
    }

    boolean isBooleanReference() {
	    return this.isBoolean() && getIsReference();
    }

    boolean isReference() {
	    return this.getIsReference();
    }

	@Override
    public TypeDescriptor getType() {
	    return this.getTypeDescriptor();
    }

}
