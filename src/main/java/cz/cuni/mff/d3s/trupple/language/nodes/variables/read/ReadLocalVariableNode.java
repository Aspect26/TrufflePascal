package cz.cuni.mff.d3s.trupple.language.nodes.variables.read;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.UnexpectedRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeFields({
    @NodeField(name = "slot", type = FrameSlot.class),
    @NodeField(name = "typeDescriptor", type = TypeDescriptor.class),
})
public abstract class ReadLocalVariableNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();

	protected abstract TypeDescriptor getTypeDescriptor();

    @Specialization(guards = "isInt()")
    int readInt(VirtualFrame frame) {
        try {
            return frame.getInt(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

	@Specialization(guards = "isLong()")
    long readLong(VirtualFrame frame) {
        try {
            return frame.getLong(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isDouble()")
    double readDouble(VirtualFrame frame) {
        try {
            return frame.getDouble(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isChar()")
    char readChar(VirtualFrame frame) {
        try {
            return (char) frame.getByte(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization(guards = "isBoolean()")
    boolean readBoolean(VirtualFrame frame) {
        try {
            return frame.getBoolean(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new UnexpectedRuntimeException();
        }
    }

    @Specialization
    Object readGeneric(VirtualFrame frame) {
	    return frame.getValue(getSlot());
    }


	@Override
    public TypeDescriptor getType() {
	    return this.getTypeDescriptor();
    }

}
