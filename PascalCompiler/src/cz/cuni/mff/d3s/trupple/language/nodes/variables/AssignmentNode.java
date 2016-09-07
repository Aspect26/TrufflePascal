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
		frame.setLong(getSlot(), value);
		return value;
	}

	@Specialization(guards = "isBoolKind(frame)")
	protected boolean writeBoolean(VirtualFrame frame, boolean value) {
		frame.setBoolean(getSlot(), value);
		return value;
	}

	// NOTE: characters are stored as bytes, since there is no FrameSlotKind for
	// char
	@Specialization(guards = "isCharKind(frame)")
	protected char writeChar(VirtualFrame frame, char value) {
		frame.setByte(getSlot(), (byte) value);
		return value;
	}

	@Specialization(guards = "isDoubleKind(frame)")
	protected double writeChar(VirtualFrame frame, double value) {
		frame.setDouble(getSlot(), value);
		return value;
	}
	
	@Specialization(guards = "isEnum(frame)")
	protected Object writeChar(VirtualFrame frame, EnumValue value) {
		try { 
			if (((EnumValue)frame.getObject(getSlot())).getEnumType() != value.getEnumType()) {
				throw new PascalRuntimeException("Wrong enum types assignment.");
			}
		} catch (FrameSlotTypeException e) {
			
		}
		frame.setObject(getSlot(), value);
		return value;
	}
	
	@Specialization(guards = "isPascalArray(frame)")
	protected Object assignArray(VirtualFrame frame, PascalArray array) {
		PascalArray arrayCopy = array.createCopy();
		frame.setObject(getSlot(), arrayCopy);
		return arrayCopy;
	}
	
	/**
	 * guard functions
	 */
	protected boolean isPascalArray(VirtualFrame frame) {
		try {
			Object obj = frame.getObject(getSlot());
			return obj instanceof PascalArray;
		} catch (FrameSlotTypeException e) {
			return false;
		}
	}
	
	protected boolean isEnum(VirtualFrame frame) {
		try {
			Object obj = frame.getObject(getSlot());
			return obj instanceof EnumValue;
		} catch (FrameSlotTypeException e) {
			return false;
		}
	}
	
	protected boolean isLongKind(VirtualFrame frame) {
		return isKind(FrameSlotKind.Long);
	}

	protected boolean isBoolKind(VirtualFrame frame) {
		return isKind(FrameSlotKind.Boolean);
	}

	protected boolean isCharKind(VirtualFrame frame) {
		return isKind(FrameSlotKind.Byte);
	}

	protected boolean isDoubleKind(VirtualFrame frame) {
		return isKind(FrameSlotKind.Double);
	}

	private boolean isKind(FrameSlotKind kind) {
		if (getSlot().getKind() == kind) {
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
