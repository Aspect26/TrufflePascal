package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

// TODO: omg refactor this class
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class ReadVariableNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();

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

}
