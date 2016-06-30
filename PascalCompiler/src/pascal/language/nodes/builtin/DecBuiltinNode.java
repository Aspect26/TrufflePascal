package pascal.language.nodes.builtin;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "dec")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class DecBuiltinNode extends BuiltinNode{

	protected abstract FrameSlot getSlot();
	
	@Specialization
	protected long decLong(VirtualFrame frame, long value){
		try {
			frame.setLong(getSlot(), frame.getLong(getSlot()) - 1);
		} catch (FrameSlotTypeException e) {
			// TODO type exception
		}
		return value;
	}
}
