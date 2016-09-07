package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class ReadVariableNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected long readLong(VirtualFrame frame) throws FrameSlotTypeException {
		VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
		if (slotsFrame == null) {
			throw new FrameSlotTypeException();
		}
		return slotsFrame.getLong(getSlot());
	}
	
	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected boolean readBool(VirtualFrame frame) throws FrameSlotTypeException {
		VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
		if (slotsFrame == null) {
			throw new FrameSlotTypeException();
		}
		return slotsFrame.getBoolean(getSlot());
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected char readChar(VirtualFrame frame) throws FrameSlotTypeException {
		VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
		if (slotsFrame == null) {
			throw new FrameSlotTypeException();
		}
		return (char) (slotsFrame.getByte(getSlot()));
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected double readDouble(VirtualFrame frame) throws FrameSlotTypeException {
		VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
		if (slotsFrame == null) {
			throw new FrameSlotTypeException();
		}
		return slotsFrame.getDouble(getSlot());
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected Object readObject(VirtualFrame frame) throws FrameSlotTypeException {
		VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
		if (slotsFrame == null) {
			throw new FrameSlotTypeException();
		}
		return slotsFrame.getObject(getSlot());
	}
}
