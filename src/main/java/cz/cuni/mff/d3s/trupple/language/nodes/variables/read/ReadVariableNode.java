package cz.cuni.mff.d3s.trupple.language.nodes.variables.read;

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

    @Override
    protected boolean isReference() {
	    return this.getIsReference();
    }

	@Override
    public TypeDescriptor getType() {
	    return this.getTypeDescriptor();
    }

}
