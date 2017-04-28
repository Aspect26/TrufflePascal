package cz.cuni.mff.d3s.trupple.language.nodes.statement;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;

public abstract class StatementNode extends Node {

	public abstract void executeVoid(VirtualFrame frame);

	public boolean verifyChildrenNodeTypes() {
	    return true;
    }
	
	protected VirtualFrame getFrameContainingSlot(VirtualFrame currentFrame, FrameSlot slot) {
		if(frameContainsSlot(currentFrame, slot)) {
			return currentFrame;
		}
		
		while(currentFrame.getArguments().length > 0) {
			currentFrame = (VirtualFrame)currentFrame.getArguments()[0];
			if(frameContainsSlot(currentFrame, slot)) {
				return currentFrame;
			}
		}
		
		return null;
	}
	
	private boolean frameContainsSlot(VirtualFrame frame, FrameSlot slot) {
		return frame.getFrameDescriptor().getSlots().contains(slot);
	}

    protected Object getValueFromSlot(VirtualFrame frame, FrameSlot slot) {
	    if (slot.getKind() == FrameSlotKind.Byte) {
            try {
                return (char) frame.getByte(slot);
            } catch (FrameSlotTypeException e) {
                throw new PascalRuntimeException("Unexpected error");
            }
        } else {
	        return frame.getValue(slot);
        }
    }

}
