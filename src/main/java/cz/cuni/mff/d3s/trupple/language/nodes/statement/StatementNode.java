package cz.cuni.mff.d3s.trupple.language.nodes.statement;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import cz.cuni.mff.d3s.trupple.language.PascalTypes;

/**
 * This class is an abstract class for each node that represent a statement.
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

    /**
     * Gets the number of required jumps to the parent frame to get frame containing specified slot. This function is
     * used for optimization. If we have some node that uses frame slot which is not from its frame then first time it
     * is executed it has to look in which frame the slot belongs, remember the number of jumps and each next time it is
     * executed it directly jumps by that number and retrieves the slot. This is thanks to the fact that the number of jumps
     * cannot change.
     * @param currentFrame the initial frame
     * @param slot the queried slot
     * @return the number of jumps to parent frame to find the queried slot
     */
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


    /**
     * Checks whether specified frame contains specified slot.
     * @param frame the frame
     * @param slot the slot
     */
    private boolean frameContainsSlot(VirtualFrame frame, FrameSlot slot) {
		return frame.getFrameDescriptor().getSlots().contains(slot);
	}

}
