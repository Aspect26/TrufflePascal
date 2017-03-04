package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeField(name = "slotKind", type = FrameSlotKind.class)
public abstract class ReadSubroutineArgumentNode extends ExpressionNode {

	protected abstract FrameSlotKind getSlotKind();

	private final int index;

	public ReadSubroutineArgumentNode(int index) {
		// NOTE the index is increased because the first argument is the virtual frame from caller method
		this.index = index + 1;
	}

	@Specialization(guards = "isLongKind()")
	protected long readLong(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if (index < args.length) {
			return (long) args[index];
		}

		throw new RuntimeException("Wrong number of parmeters passed.");
	}

	@Specialization(guards = "isBoolKind()")
	protected boolean readBoolean(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if (index < args.length) {
			return (boolean) args[index];
		}

		throw new RuntimeException("Wrong number of parmeters passed.");
	}

	@Specialization(guards = "isCharKind()")
	protected char readChar(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if (index < args.length) {
			return (char) args[index];
		}

		throw new RuntimeException("Wrong number of parmeters passed.");
	}

	@Specialization(guards = "isDoubleKind()")
	protected double readDouble(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if (index < args.length) {
			return (double) args[index];
		}

		throw new RuntimeException("Wrong number of parmeters passed.");
	}

	@Specialization
	protected Object readObject(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if (index < args.length) {
			return args[index];
		}

		throw new RuntimeException("Wrong number of parmeters passed.");
	}

	protected boolean isLongKind() {
		return getSlotKind() == FrameSlotKind.Long;
	}

	protected boolean isBoolKind() {
		return getSlotKind() == FrameSlotKind.Boolean;
	}

	protected boolean isCharKind() {
		return getSlotKind() == FrameSlotKind.Byte;
	}

    protected boolean isDoubleKind() {
        return getSlotKind() == FrameSlotKind.Double;
    }

}
