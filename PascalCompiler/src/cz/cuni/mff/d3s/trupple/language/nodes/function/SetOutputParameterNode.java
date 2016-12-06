package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;

public class SetOutputParameterNode extends StatementNode{

	private final FrameSlot callerSlot;
	private final FrameSlot calleeSlot;
	
	public SetOutputParameterNode(FrameSlot callerSlot, FrameSlot calleeSlot) {
		this.callerSlot = callerSlot;
		this.calleeSlot = calleeSlot;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		VirtualFrame callerFrame = (VirtualFrame)frame.getArguments()[0];
		trySetCallerSlot(callerFrame, frame);
	}
	
	private void trySetCallerSlot(VirtualFrame callerFrame, VirtualFrame calleeFrame) {
		try {
			setCallerSlot(callerFrame, calleeFrame);
		} catch (FrameSlotTypeException e) {
			throw new PascalRuntimeException("Frame slot type eception at SetOutputParameterNode.");
		}
	}
	
	private void setCallerSlot(VirtualFrame callerFrame, VirtualFrame calleeFrame) throws FrameSlotTypeException {
		switch(callerSlot.getKind()){
		case Object:
			setObject(callerFrame, calleeFrame.getObject(this.calleeSlot));
		case Long:
			setLong(callerFrame, calleeFrame.getLong(this.calleeSlot));
		case Boolean:
			setBoolean(callerFrame, calleeFrame.getBoolean(this.calleeSlot));
		case Byte:
			setByte(callerFrame, calleeFrame.getByte(this.calleeSlot));
		case Double:
			setDouble(callerFrame, calleeFrame.getDouble(this.calleeSlot));
		default:
			throw new PascalRuntimeException("UnknownDescriptor slot type in setting output parameter: " + callerSlot.getKind() + ".");
		}
	}
	
	private void setBoolean(VirtualFrame callerFrame, boolean value) {
		callerFrame.setBoolean(this.callerSlot, value);
	}
	
	private void setByte(VirtualFrame callerFrame, byte value) {
		callerFrame.setByte(this.callerSlot, value);
	}
	
	private void setDouble(VirtualFrame callerFrame, double value) {
		callerFrame.setDouble(this.callerSlot, value);
	}
	
	private void setLong(VirtualFrame callerFrame, long value) {
		callerFrame.setLong(this.callerSlot, value);
	}
	
	private void setObject(VirtualFrame callerFrame, Object value) {
		callerFrame.setObject(this.callerSlot, value);
	}
}
