package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

public class InitializationNodeFactory{

	public static StatementNode create(FrameSlot frameSlot, Object value, VirtualFrame frame) {
	    // TODO: this is a duplicity
        switch (frameSlot.getKind()) {
            case Int: return (frame == null)?
                    new IntInitializationNode(frameSlot, (int) value) : new IntInitializationWithFrameNode(frameSlot, (int) value, frame);
            case Long: return (frame == null)?
                    new LongInitializationNode(frameSlot, (long) value) : new LongInitializationWithFrameNode(frameSlot, (long) value, frame);
            case Double: return (frame == null)?
                    new DoubleInitializationNode(frameSlot, (double) value) : new DoubleInitializationWithFrameNode(frameSlot, (double) value, frame);
            case Boolean: return (frame == null)?
                    new BooleanInitializationNode(frameSlot, (boolean) value) : new BooleanInitializationWithFrameNode(frameSlot, (boolean) value, frame);
            case Byte: return (frame == null)?
                    new CharInitializationNode(frameSlot, (char) value) : new CharInitializationWithFrameNode(frameSlot, (char) value, frame);
            default: return (frame == null)?
                    new ObjectInitializationNode(frameSlot, value) : new ObjectInitializationWithFrameNode(frameSlot, value, frame);
        }
	}
}

abstract class InitializationNode extends StatementNode {
	
	protected final FrameSlot slot;

	InitializationNode(FrameSlot slot) {
		this.slot = slot;
	}
	
	@Override
	public abstract void executeVoid(VirtualFrame frame);
}

class IntInitializationNode extends InitializationNode {

    protected final int value;

    IntInitializationNode(FrameSlot slot, int value) {
        super(slot);
        this.value = value;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        frame.setInt(slot, value);
    }

}

class IntInitializationWithFrameNode extends IntInitializationNode {

    private final VirtualFrame frame;

    IntInitializationWithFrameNode(FrameSlot slot, int value, VirtualFrame frame) {
        super(slot, value);
        this.frame = frame;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        this.frame.setInt(slot, value);
    }

}

class LongInitializationNode extends InitializationNode {

	protected final long value;
	
	LongInitializationNode(FrameSlot slot, long value) {
		super(slot);
		this.value = value;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
        frame.setLong(slot, value);
	}

}

class LongInitializationWithFrameNode extends LongInitializationNode {

    private final VirtualFrame frame;

    LongInitializationWithFrameNode(FrameSlot slot, long value, VirtualFrame frame) {
        super(slot, value);
        this.frame = frame;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        this.frame.setLong(slot, value);
    }

}

class CharInitializationNode extends InitializationNode {

	protected final char value;
	
	CharInitializationNode(FrameSlot slot, char value) {
		super(slot);
		this.value = value;
	}

    @Override
    public void executeVoid(VirtualFrame frame) {
        frame.setByte(slot, (byte)value);
    }

}

class CharInitializationWithFrameNode extends CharInitializationNode {

    private final VirtualFrame frame;

    CharInitializationWithFrameNode(FrameSlot slot, char value, VirtualFrame frame) {
        super(slot, value);
        this.frame = frame;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        this.frame.setByte(slot, (byte) value);
    }

}

class DoubleInitializationNode extends InitializationNode {

	protected final double value;
	
	DoubleInitializationNode(FrameSlot slot, double value) {
		super(slot);
		this.value = value;
	}

    @Override
    public void executeVoid(VirtualFrame frame) {
        frame.setDouble(slot, value);
    }

}

class DoubleInitializationWithFrameNode extends DoubleInitializationNode {

    private final VirtualFrame frame;

    DoubleInitializationWithFrameNode(FrameSlot slot, double value, VirtualFrame frame) {
        super(slot, value);
        this.frame = frame;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        this.frame.setDouble(slot, value);
    }

}

class BooleanInitializationNode extends InitializationNode {

	protected final boolean value;
	
	BooleanInitializationNode(FrameSlot slot, boolean value) {
		super(slot);
		this.value = value;
	}

    @Override
    public void executeVoid(VirtualFrame frame) {
        frame.setBoolean(slot, value);
    }

}

class BooleanInitializationWithFrameNode extends BooleanInitializationNode {

    private final VirtualFrame frame;

    BooleanInitializationWithFrameNode(FrameSlot slot, boolean value, VirtualFrame frame) {
        super(slot, value);
        this.frame = frame;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        this.frame.setBoolean(slot, value);
    }

}

class ObjectInitializationNode extends InitializationNode {

	protected final Object value;
	
	ObjectInitializationNode(FrameSlot slot, Object value) {
		super(slot);
		this.value = value;
	}

    @Override
    public void executeVoid(VirtualFrame frame) {
        frame.setObject(slot, value);
    }

}

class ObjectInitializationWithFrameNode extends ObjectInitializationNode {

    private final VirtualFrame frame;

    ObjectInitializationWithFrameNode(FrameSlot slot, Object value, VirtualFrame frame) {
        super(slot, value);
        this.frame = frame;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        this.frame.setObject(slot, value);
    }

}