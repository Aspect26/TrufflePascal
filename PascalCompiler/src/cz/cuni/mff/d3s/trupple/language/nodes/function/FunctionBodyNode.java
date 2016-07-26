package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class FunctionBodyNode extends ExpressionNode {
	
	protected abstract FrameSlot getSlot();
	
	/** The body. */
	@Child private StatementNode bodyNode;
	
	public FunctionBodyNode(StatementNode bodyNode){
		this.bodyNode = bodyNode;
	}

	@Specialization(guards = "isLongKind()")
	public long execLong(VirtualFrame frame) {
		bodyNode.executeVoid(frame);
		
		try{
			return frame.getLong(getSlot());
		} catch(FrameSlotTypeException e)  {
			return -1;
		}
	}
	
	@Specialization(guards = "isBoolKind()")
	public boolean execBool(VirtualFrame frame) {
		bodyNode.executeVoid(frame);
		
		try{
			return frame.getBoolean(getSlot());
		} catch(FrameSlotTypeException e)  {
			return false;
		}
	}
	
	@Specialization(guards = "isCharKind()")
	public char execChar(VirtualFrame frame) {
		bodyNode.executeVoid(frame);
		
		try{
			return (char)frame.getByte(getSlot());
		} catch(FrameSlotTypeException e)  {
			return '0';
		}
	}
	
	protected boolean isLongKind(){
		return getSlot().getKind() == FrameSlotKind.Long;
	}
	
	protected boolean isBoolKind(){
		return getSlot().getKind() == FrameSlotKind.Boolean;
	}
	
	protected boolean isCharKind(){
		return getSlot().getKind() == FrameSlotKind.Byte;
	}
}
