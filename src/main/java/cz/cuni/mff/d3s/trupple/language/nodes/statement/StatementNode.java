package cz.cuni.mff.d3s.trupple.language.nodes.statement;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import cz.cuni.mff.d3s.trupple.language.PascalTypes;

/**
 * This class is a parent class for each nodes that represent a statement
 */
@TypeSystemReference(PascalTypes.class)
public abstract class StatementNode extends Node {

    /**
     * This method is used for compile time type checking. Each node that must be verified that it got children
     * nodes of right types (mostly operation nodes) implements its type checking in this method.
     * @return true if the children nodes are of the required type, false otherwise
     */
    public boolean verifyChildrenNodeTypes() {
        return true;
    }

    /**
     * Executes the statement node without returning any value
     * @param frame current frame
     */
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

    protected int getJumpsToFrame(VirtualFrame currentFrame, FrameSlot slot) {
	    int result = 0;
        if (frameContainsSlot(currentFrame, slot)) {
            return result;
        }

        while (currentFrame.getArguments().length > 0) {
            currentFrame = (VirtualFrame) currentFrame.getArguments()[0];
            ++result;
            if (frameContainsSlot(currentFrame, slot)) {
                return result;
            }
        }

        return -1;
    }


    private boolean frameContainsSlot(VirtualFrame frame, FrameSlot slot) {
		return frame.getFrameDescriptor().getSlots().contains(slot);
	}

}
