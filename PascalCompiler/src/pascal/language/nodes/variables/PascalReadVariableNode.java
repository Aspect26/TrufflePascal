package pascal.language.nodes.variables;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;

import pascal.language.nodes.PascalNode;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class PascalReadVariableNode extends PascalNode{

	protected abstract FrameSlot getSlot();
	
	@Specialization(rewriteOn = FrameSlotTypeException.class)
    protected long readLong(VirtualFrame frame) throws FrameSlotTypeException {
        return frame.getLong(getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected boolean readBoolean(VirtualFrame frame) throws FrameSlotTypeException {
        return frame.getBoolean(getSlot());
    }

    @Specialization(rewriteOn = FrameSlotTypeException.class)
    protected Object readObject(VirtualFrame frame) throws FrameSlotTypeException {
        return frame.getObject(getSlot());
    }

    /**
     * This is the generic case that always succeeds.
     */
    @Specialization(contains = {"readLong", "readBoolean", "readObject"})
    protected Object read(VirtualFrame frame) {
        return frame.getValue(getSlot());
    }
	
}
