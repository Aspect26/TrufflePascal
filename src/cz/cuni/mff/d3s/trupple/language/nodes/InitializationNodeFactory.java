package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class InitializationNodeFactory{

	public static StatementNode create(FrameSlot frameSlot, Object value, VirtualFrame frame) {
	    // TODO: this is a duplicity
        switch (frameSlot.getKind()) {
            case Long: return new LongInitializationNode(frameSlot, (long) value, frame);
            case Double: return new DoubleInitializationNode(frameSlot, (double) value, frame);
            case Boolean: return new BooleanInitializationNode(frameSlot, (boolean) value, frame);
            case Byte: return new CharInitializationNode(frameSlot, (char) value, frame);
            default: return new ObjectInitializationNode(frameSlot, value, frame);
        }
	}
}

abstract class InitializationNode extends StatementNode {
	
	protected final FrameSlot slot;
	protected final VirtualFrame frame;
	
	InitializationNode(FrameSlot slot, VirtualFrame frame) {
		this.slot = slot;
		this.frame = frame;
	}
	
	@Override
	public abstract void executeVoid(VirtualFrame frame);
}

class LongInitializationNode extends InitializationNode {

	private final long value;
	
	LongInitializationNode(FrameSlot slot, long value, VirtualFrame frame) {
		super(slot, frame);
		this.value = value;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
	    if (this.frame != null) {
            this.frame.setLong(slot, value);
        } else {
            frame.setLong(slot, value);
        }
	}

}

class CharInitializationNode extends InitializationNode {

	private final char value;
	
	CharInitializationNode(FrameSlot slot, char value, VirtualFrame frame) {
		super(slot, frame);
		this.value = value;
	}

    @Override
    public void executeVoid(VirtualFrame frame) {
        if (this.frame != null) {
            this.frame.setByte(slot, (byte)value);
        } else {
            frame.setByte(slot, (byte)value);
        }
    }

}

class DoubleInitializationNode extends InitializationNode {

	private final double value;
	
	DoubleInitializationNode(FrameSlot slot, double value, VirtualFrame frame) {
		super(slot, frame);
		this.value = value;
	}

    @Override
    public void executeVoid(VirtualFrame frame) {
        if (this.frame != null) {
            this.frame.setDouble(slot, value);
        } else {
            frame.setDouble(slot, value);
        }
    }

}

class BooleanInitializationNode extends InitializationNode {

	private final boolean value;
	
	BooleanInitializationNode(FrameSlot slot, boolean value, VirtualFrame frame) {
		super(slot, frame);
		this.value = value;
	}

    @Override
    public void executeVoid(VirtualFrame frame) {
        if (this.frame != null) {
            this.frame.setBoolean(slot, value);
        } else {
            frame.setBoolean(slot, value);
        }
    }

}

class ObjectInitializationNode extends InitializationNode {

	private final Object value;
	
	ObjectInitializationNode(FrameSlot slot, Object value, VirtualFrame frame) {
		super(slot, frame);
		this.value = value;
	}

    @Override
    public void executeVoid(VirtualFrame frame) {
        if (this.frame != null) {
            this.frame.setObject(slot, value);
        } else {
            frame.setObject(slot, value);
        }
    }

}
