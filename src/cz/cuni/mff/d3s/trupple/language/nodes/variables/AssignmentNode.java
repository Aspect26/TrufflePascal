package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AssignmentNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();

	@Specialization(guards = "isLongKind(frame)")
	protected long writeLong(VirtualFrame frame, long value) {
        VirtualFrame slotFrame = getFrameContainingSlot(frame, getSlot());
	    Reference referenceVariable = this.tryGetReference(slotFrame, getSlot());
	    if (referenceVariable == null) {
            slotFrame.setLong(getSlot(), value);
        } else {
	        referenceVariable.getFromFrame().setLong(referenceVariable.getFrameSlot(), value);
        }
		return value;
	}

	@Specialization(guards = "isBoolKind(frame)")
	protected boolean writeBoolean(VirtualFrame frame, boolean value) {
        VirtualFrame slotFrame = getFrameContainingSlot(frame, getSlot());
        Reference referenceVariable = this.tryGetReference(slotFrame, getSlot());
        if (referenceVariable == null) {
            slotFrame.setBoolean(getSlot(), value);
        } else {
            referenceVariable.getFromFrame().setBoolean(referenceVariable.getFrameSlot(), value);
        }
        return value;
	}

	// NOTE: characters are stored as bytes, since there is no FrameSlotKind for
	// char
	@Specialization(guards = "isCharKind(frame)")
	protected char writeChar(VirtualFrame frame, char value) {
        VirtualFrame slotFrame = getFrameContainingSlot(frame, getSlot());
        Reference referenceVariable = this.tryGetReference(slotFrame, getSlot());
        if (referenceVariable == null) {
            slotFrame.setByte(getSlot(), (byte)value);
        } else {
            referenceVariable.getFromFrame().setByte(referenceVariable.getFrameSlot(), (byte)value);
        }
        return value;
	}

	@Specialization(guards = "isDoubleKind(frame)")
	protected double writeDouble(VirtualFrame frame, double value) {
        VirtualFrame slotFrame = getFrameContainingSlot(frame, getSlot());
        Reference referenceVariable = this.tryGetReference(slotFrame, getSlot());
        if (referenceVariable == null) {
            slotFrame.setDouble(getSlot(), value);
        } else {
            referenceVariable.getFromFrame().setDouble(referenceVariable.getFrameSlot(), value);
        }
        return value;
	}
	
	@Specialization(guards = "isEnum(frame)")
	protected Object writeEnum(VirtualFrame frame, EnumValue value) {
        VirtualFrame slotFrame = getFrameContainingSlot(frame, getSlot());
        Reference referenceVariable = this.tryGetReference(slotFrame, getSlot());
        if (referenceVariable == null) {
            slotFrame.setObject(getSlot(), value);
        } else {
            referenceVariable.getFromFrame().setObject(referenceVariable.getFrameSlot(), value);
        }
        return value;
	}
	
	@Specialization(guards = "isPascalArray(frame)")
	protected Object assignArray(VirtualFrame frame, PascalArray array) {
		PascalArray arrayCopy = array.createDeepCopy();

        VirtualFrame slotFrame = getFrameContainingSlot(frame, getSlot());
        Reference referenceVariable = this.tryGetReference(slotFrame, getSlot());
        if (referenceVariable == null) {
            slotFrame.setObject(getSlot(), arrayCopy);
        } else {
            referenceVariable.getFromFrame().setObject(referenceVariable.getFrameSlot(), arrayCopy);
        }
        return arrayCopy;
	}

	/**
	 * guard functions
	 */
	protected boolean isPascalArray(VirtualFrame frame) {
		frame = this.getFrameContainingSlot(frame, getSlot());
		if(frame == null) {
			return false;
		}
		try {
			Object obj = frame.getObject(getSlot());
			return obj instanceof PascalArray;
		} catch (FrameSlotTypeException e) {
			return false;
		}
	}
	
	protected boolean isEnum(VirtualFrame frame) {
		frame = this.getFrameContainingSlot(frame, getSlot());
		if(frame == null) {
			return false;
		}
		try {
			Object obj = frame.getObject(getSlot());
			return obj instanceof EnumValue;
		} catch (FrameSlotTypeException e) {
			return false;
		}
	}
	
	protected boolean isLongKind(VirtualFrame frame) {
		return isKind(frame, FrameSlotKind.Long);
	}

	protected boolean isBoolKind(VirtualFrame frame) {
		return isKind(frame, FrameSlotKind.Boolean);
	}

	protected boolean isCharKind(VirtualFrame frame) {
		return isKind(frame, FrameSlotKind.Byte);
	}

	protected boolean isDoubleKind(VirtualFrame frame) {
		return isKind(frame, FrameSlotKind.Double);
	}

	private boolean isKind(VirtualFrame frame, FrameSlotKind kind) {
		frame = getFrameContainingSlot(frame, getSlot());
		if (frame == null)
			return false;

		Reference reference = this.tryGetReference(frame, getSlot());
		if (reference == null) {
			return getSlot().getKind() == kind;
		}
		else {
			return reference.getFrameSlot().getKind() == kind;
		}
	}

	private Reference tryGetReference(VirtualFrame frame, FrameSlot slot) {
		try {
			return (Reference)frame.getObject(slot);
		} catch (FrameSlotTypeException | ClassCastException e) {
			return null;
		}
	}
}
