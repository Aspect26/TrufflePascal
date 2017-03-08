package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AssignmentNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();

	@Specialization(guards = "isLongKind(frame, getSlot())")
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

	@Specialization(guards = "isBoolKind(frame, getSlot())")
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
	@Specialization(guards = "isCharKind(frame, getSlot())")
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

	@Specialization(guards = "isDoubleKind(frame, getSlot())")
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
	
	@Specialization(guards = "isEnum(frame, getSlot())")
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
	
	@Specialization(guards = "isPascalArray(frame, getSlot())")
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

    @Specialization(guards = "isSet(frame, getSlot())")
    protected Object assignSet(VirtualFrame frame, SetTypeValue set) {
        SetTypeValue setCopy = set.createDeepCopy();

        VirtualFrame slotFrame = getFrameContainingSlot(frame, getSlot());
        Reference referenceVariable = this.tryGetReference(slotFrame, getSlot());
        if (referenceVariable == null) {
            slotFrame.setObject(getSlot(), setCopy);
        } else {
            referenceVariable.getFromFrame().setObject(referenceVariable.getFrameSlot(), setCopy);
        }
        return setCopy;
    }

}
