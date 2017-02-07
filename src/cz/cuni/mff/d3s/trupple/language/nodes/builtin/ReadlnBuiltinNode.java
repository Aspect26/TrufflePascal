package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import java.io.IOException;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "readln")
public class ReadlnBuiltinNode extends BuiltinNode {

	private final PascalContext context;
	private final FrameSlot[] readSlots;
	
	public ReadlnBuiltinNode(PascalContext context, FrameSlot[] readSlots){
		this.context = context;
		this.readSlots = readSlots;
	}
	
	public ReadlnBuiltinNode(PascalContext context){
		this(context, null);
	}
	
	@Override
	public PascalContext getContext() {
		return this.context;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(this.readSlots == null) {
			executeParameterless();
		} else {
			executeWithParameters(frame);
		}
		
		return null;
	}
	
	private void executeWithParameters(VirtualFrame frame) {
		for(FrameSlot readSlot : this.readSlots){
			setFrameSlot(frame, readSlot);
		}
	}
	
	private void executeParameterless() {
		this.readLine();
	}
	
	private void setFrameSlot(VirtualFrame frame, FrameSlot slot){
		String inputString = this.readLine();
		setFrameSlot(frame, slot, inputString);
	}
	
	private String readLine(){
		try {
			return this.context.getInput().readLine();
		} catch (IOException e) {
			throw new PascalRuntimeException("Can not read input.");
		}
	}
	
	private void setFrameSlot(VirtualFrame frame, FrameSlot slot, String value){
		switch(slot.getKind()){
		case Boolean:
			frame.setBoolean(slot, Boolean.valueOf(value)); break;
		case Byte:
			frame.setByte(slot, Byte.valueOf(value)); break;
		case Double:
			frame.setDouble(slot, Double.valueOf(value)); break;
		case Long:
			frame.setLong(slot, Long.valueOf(value)); break;
		default:
			throw new PascalRuntimeException("Wrong object passed to readln: " + slot.getIdentifier());
		}
	}
}
