package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

import java.util.List;

public class InitializationNodeFactory{

	public static StatementNode create(FrameSlot frameSlot, long numericValue) {
		return new LongInitializationNode(frameSlot, numericValue);
	}
	
	public static StatementNode create(FrameSlot frameSlot, double doubleValue) {
		return new DoubleInitializationNode(frameSlot, doubleValue);
	}
	
	public static StatementNode create(FrameSlot frameSlot, boolean booleanValue) {
		return new BooleanInitializationNode(frameSlot, booleanValue);
	}
	
	public static StatementNode create(FrameSlot frameSlot, char byteValue) {
		return new CharInitializationNode(frameSlot, byteValue);
	}
	
	public static StatementNode create(FrameSlot frameSlot, Object numericValue) {
		return new ObjectInitializationNode(frameSlot, numericValue);
	}
}

abstract class InitializationNode extends StatementNode {
	
	protected final FrameSlot slot;
	
	public InitializationNode(FrameSlot slot) {
		this.slot = slot;
	}
	
	@Override
	public abstract void executeVoid(VirtualFrame frame);
}

class LongInitializationNode extends InitializationNode {

	private final long value;
	
	public LongInitializationNode(FrameSlot slot, long value) {
		super(slot);
		this.value = value;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		frame.setLong(slot, value);
	}
}

class CharInitializationNode extends InitializationNode {

	private final char value;
	
	public CharInitializationNode(FrameSlot slot, char value) {
		super(slot);
		this.value = value;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		frame.setByte(slot, (byte)value);
	}
}

class DoubleInitializationNode extends InitializationNode {

	private final double value;
	
	public DoubleInitializationNode(FrameSlot slot, double value) {
		super(slot);
		this.value = value;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		frame.setDouble(slot, value);
	}
}

class BooleanInitializationNode extends InitializationNode {

	private final boolean value;
	
	public BooleanInitializationNode(FrameSlot slot, boolean value) {
		super(slot);
		this.value = value;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		frame.setBoolean(slot, value);
	}
}

class ObjectInitializationNode extends InitializationNode {

	private final Object value;
	
	public ObjectInitializationNode(FrameSlot slot, Object value) {
		super(slot);
		this.value = value;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		frame.setObject(slot, value);
	}
}
