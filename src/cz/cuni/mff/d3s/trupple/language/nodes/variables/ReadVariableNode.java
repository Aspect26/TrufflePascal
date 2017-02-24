package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
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
		try {
			return slotsFrame.getLong(getSlot());
		} catch (FrameSlotTypeException e) {
			// TODO: these tryGetReference() calls may cause permormanfe slowdown
			Reference referenceObject = tryGetReference(slotsFrame);
			return referenceObject.getFromFrame().getLong(referenceObject.getFrameSlot());
		}
	}
	
	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected boolean readBool(VirtualFrame frame) throws FrameSlotTypeException {
		VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
		if (slotsFrame == null) {
			throw new FrameSlotTypeException();
		}
		try {
			return slotsFrame.getBoolean(getSlot());
		} catch (FrameSlotTypeException e) {
			// TODO: these tryGetReference() calls may cause permormanfe slowdown
			Reference referenceObject = tryGetReference(slotsFrame);
			return referenceObject.getFromFrame().getBoolean(referenceObject.getFrameSlot());
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected char readChar(VirtualFrame frame) throws FrameSlotTypeException {
		VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
		if (slotsFrame == null) {
			throw new FrameSlotTypeException();
		}
		try {
			return (char) (slotsFrame.getByte(getSlot()));
		} catch (FrameSlotTypeException e) {
			// TODO: these tryGetReference() calls may cause permormanfe slowdown
			Reference referenceObject = tryGetReference(slotsFrame);
			return (char) referenceObject.getFromFrame().getByte(referenceObject.getFrameSlot());
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected double readDouble(VirtualFrame frame) throws FrameSlotTypeException {
		VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
		if (slotsFrame == null) {
			throw new FrameSlotTypeException();
		}
		try {
			return slotsFrame.getDouble(getSlot());
		} catch (FrameSlotTypeException e) {
			// TODO: these tryGetReference() calls may cause permormanfe slowdown
			Reference referenceObject = tryGetReference(slotsFrame);
			return referenceObject.getFromFrame().getDouble(referenceObject.getFrameSlot());
		}
	}

	@Specialization(rewriteOn = FrameSlotTypeException.class)
	protected Object readObject(VirtualFrame frame) throws FrameSlotTypeException {
		VirtualFrame slotsFrame = this.getFrameContainingSlot(frame, getSlot());
		if (slotsFrame == null) {
			throw new FrameSlotTypeException();
		}
		try {
			return slotsFrame.getObject(getSlot());
		} catch (FrameSlotTypeException e) {
			// TODO: these tryGetReference() calls may cause permormanfe slowdown
			Reference referenceObject = tryGetReference(slotsFrame);
			return referenceObject.getFromFrame().getObject(referenceObject.getFrameSlot());
		}
	}

	private Reference tryGetReference(VirtualFrame frame) throws FrameSlotTypeException {
		Object mayBereferenceObject = frame.getObject(getSlot());
		try {
			return (Reference)mayBereferenceObject;
		} catch (ClassCastException e) {
			throw new FrameSlotTypeException();
		}
	}
}
