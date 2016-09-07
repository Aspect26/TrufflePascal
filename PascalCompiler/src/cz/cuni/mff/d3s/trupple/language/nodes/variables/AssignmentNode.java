package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.CompilerDirectives;
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
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AssignmentNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();

	@Specialization(guards = "isLongKind(frame)")
	protected long writeLong(VirtualFrame frame, long value) {
		VirtualFrame slotsFrame = getFrameContainingSlot(frame, getSlot());
		slotsFrame.setLong(getSlot(), value);
		return value;
	}

	@Specialization(guards = "isBoolKind(frame)")
	protected boolean writeBoolean(VirtualFrame frame, boolean value) {
		VirtualFrame slotsFrame = getFrameContainingSlot(frame, getSlot());
		slotsFrame.setBoolean(getSlot(), value);
		return value;
	}

	// NOTE: characters are stored as bytes, since there is no FrameSlotKind for
	// char
	@Specialization(guards = "isCharKind(frame)")
	protected char writeChar(VirtualFrame frame, char value) {
		VirtualFrame slotsFrame = getFrameContainingSlot(frame, getSlot());
		slotsFrame.setByte(getSlot(), (byte) value);
		return value;
	}

	@Specialization(guards = "isDoubleKind(frame)")
	protected double writeChar(VirtualFrame frame, double value) {
		VirtualFrame slotsFrame = getFrameContainingSlot(frame, getSlot());
		slotsFrame.setDouble(getSlot(), value);
		return value;
	}
	
	@Specialization(guards = "isEnum(frame)")
	protected Object writeChar(VirtualFrame frame, EnumValue value) {
		VirtualFrame slotsFrame = getFrameContainingSlot(frame, getSlot());
		try { 
			if (((EnumValue)slotsFrame.getObject(getSlot())).getEnumType() != value.getEnumType()) {
				throw new PascalRuntimeException("Wrong enum types assignment.");
			}
		} catch (FrameSlotTypeException e) {
			
		}
		slotsFrame.setObject(getSlot(), value);
		return value;
	}
	
	@Specialization(guards = "isPascalArray(frame)")
	protected Object assignArray(VirtualFrame frame, PascalArray array) {
		VirtualFrame slotsFrame = getFrameContainingSlot(frame, getSlot());
		PascalArray arrayCopy = array.createCopy();
		slotsFrame.setObject(getSlot(), arrayCopy);
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
		if (getFrameContainingSlot(frame, getSlot()) == null) {
			return false;
		} else if (getSlot().getKind() == kind) {
			return true;
		} else if (getSlot().getKind() == FrameSlotKind.Illegal) {
			CompilerDirectives.transferToInterpreterAndInvalidate();
			getSlot().setKind(kind);
			return true;
		} else {
			return false;
		}
	}

}
