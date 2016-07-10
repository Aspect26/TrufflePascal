package pascal.language.nodes.variables;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.language.nodes.ExpressionNode;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class AssignmentNode extends ExpressionNode{

	protected abstract FrameSlot getSlot();
	
	@Specialization(guards = "isLongKind(frame)")
	protected long writeLong(VirtualFrame frame, long value){
		frame.setLong(getSlot(), value);
		return value;
	}
	
	@Specialization(guards = "isBoolKind(frame)")
	protected boolean writeBoolean(VirtualFrame frame, boolean value){
		frame.setBoolean(getSlot(), value);
		return value;
	}
	
	// NOTE: characters are stored as bytes, since there is no FrameSlotKind for char
	@Specialization(guards = "isCharKind(frame)")
	protected char writeChar(VirtualFrame frame, char value){
		frame.setByte(getSlot(), (byte)value);
		return value;
	}
	
	@Specialization(guards = "isDoubleKind(frame)")
	protected double writeChar(VirtualFrame frame, double value){
		frame.setDouble(getSlot(), value);
		return value;
	}
	
	/**
	 * guard functions
	 */
	protected boolean isLongKind(VirtualFrame frame){
		return isKind(FrameSlotKind.Long);
	}
	
	protected boolean isBoolKind(VirtualFrame frame){
		return isKind(FrameSlotKind.Boolean);
	}
	
	protected boolean isCharKind(VirtualFrame frame){
		return isKind(FrameSlotKind.Byte);
	}
	
	protected boolean isDoubleKind(VirtualFrame frame){
		return isKind(FrameSlotKind.Double);
	}
	
	private boolean isKind(FrameSlotKind kind){
		if(getSlot().getKind() == kind){
			return true;
		}
		else if(getSlot().getKind() == FrameSlotKind.Illegal){
			CompilerDirectives.transferToInterpreterAndInvalidate();
            getSlot().setKind(kind);
            return true;
		}
		else{
			return false;
		}
	}
	
}
