package pascal.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.language.nodes.ExpressionNode;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class ReadVariableNode extends ExpressionNode {

	protected abstract FrameSlot getSlot();
	
	@Specialization(rewriteOn = FrameSlotTypeException.class)
    protected int readInt(VirtualFrame frame) throws FrameSlotTypeException {
        return frame.getInt(getSlot());
    }
	
	@Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong(VirtualFrame frame) throws FrameSlotTypeException {
        return frame.getLong(getSlot());
    }
	
	@Specialization(rewriteOn = FrameSlotTypeException.class)
    protected short readByte(VirtualFrame frame) throws FrameSlotTypeException {
        return frame.getByte(getSlot());
    }
	
	@Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame frame) throws FrameSlotTypeException {
        return frame.getObject(getSlot());
    }
}
