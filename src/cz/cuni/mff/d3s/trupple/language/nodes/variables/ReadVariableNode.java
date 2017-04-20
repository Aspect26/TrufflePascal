package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeFields({
    @NodeField(name = "slot", type = FrameSlot.class),
    @NodeField(name = "typeDescriptor", type = TypeDescriptor.class)
})
public abstract class ReadVariableNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();

	protected abstract TypeDescriptor getTypeDescriptor();

	// TODO: what about this @Specialization(guards = "getTypeDescriptor == LongDescriptor.getInstance()")
	@Specialization(guards = "isLongKindOrLongReference(frame, getSlot())")
	long readLong(VirtualFrame frame) {
		VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
		try {
			return slotsFrame.getLong(getSlot());
		} catch (FrameSlotTypeException e) {
			throw new PascalRuntimeException("Unexpected error");
		}
	}

	@Specialization(guards = "isBoolKindOrBoolReference(frame, getSlot())")
	boolean readBool(VirtualFrame frame) {
        VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
        try {
            return slotsFrame.getBoolean(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
	}

    @Specialization(guards = "isCharKindOrCharReference(frame, getSlot())")
	char readChar(VirtualFrame frame) {
        VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
        try {
            return (char) slotsFrame.getByte(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
	}

    @Specialization(guards = "isDoubleKindOrDoubleReference(frame, getSlot())")
	double readDouble(VirtualFrame frame) {
        VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
        try {
            return slotsFrame.getDouble(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
	}

    @Specialization
	Object readObject(VirtualFrame frame) {
        VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
        try {
            return slotsFrame.getObject(getSlot());
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
	}

	@Override
    public TypeDescriptor getType() {
	    return this.getTypeDescriptor();
    }

}
