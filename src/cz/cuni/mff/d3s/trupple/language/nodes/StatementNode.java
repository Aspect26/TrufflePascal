package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

public abstract class StatementNode extends Node {

	public abstract void executeVoid(VirtualFrame frame);
	
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
	
	protected boolean frameContainsSlot(VirtualFrame frame, FrameSlot slot) {
		return frame.getFrameDescriptor().getSlots().contains(slot);
	}

	protected FrameSlot findSlotByIdentifier(VirtualFrame frame, String identifier) {
	    for (FrameSlot frameSlot : frame.getFrameDescriptor().getSlots()) {
	        if (frameSlot.getIdentifier().equals(identifier)) {
	            return frameSlot;
            }
        }

        return null;
    }
}
