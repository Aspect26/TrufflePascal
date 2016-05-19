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
	
	@Specialization(guards = "isIntKind(frame)")
	protected int writeInt(VirtualFrame frame, int value){
		//TODO: chceck if the variable even exist!
		frame.setInt(getSlot(), value);
		return value;
	}
	
	@Specialization(guards = "isLongKind(frame)")
	protected long writeLong(VirtualFrame frame, long value){
		//TODO: chceck if the variable even exist!
		frame.setLong(getSlot(), value);
		return value;
	}
	
	
	
	/**
	 * guard functions
	 */
	protected boolean isLongKind(VirtualFrame frame){
		return isKind(FrameSlotKind.Long);
	}
	
	protected boolean isIntKind(VirtualFrame frame){
		return isKind(FrameSlotKind.Int);
	}
	
	private boolean isKind(FrameSlotKind kind){
		if(getSlot().getKind() == kind){
			return true;
		}
		else if(getSlot().getKind() == FrameSlotKind.Illegal){
			// FIRST TIME WRITE TO VAIABLE!
			CompilerDirectives.transferToInterpreterAndInvalidate();
            getSlot().setKind(kind);
            return true;
		}
		else{
			return false;
		}
	}
	
}
