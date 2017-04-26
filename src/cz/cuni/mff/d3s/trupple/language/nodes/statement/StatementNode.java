package cz.cuni.mff.d3s.trupple.language.nodes.statement;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;

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

    protected Reference tryGetReference(VirtualFrame frame, FrameSlot slot) {
        try {
            return (Reference)frame.getObject(slot);
        } catch (FrameSlotTypeException | ClassCastException e) {
            return null;
        }
    }

    protected FrameSlot findSlotByIdentifier(VirtualFrame frame, String identifier) {
        for (FrameSlot frameSlot : frame.getFrameDescriptor().getSlots()) {
            if (frameSlot.getIdentifier().equals(identifier)) {
                return frameSlot;
            }
        }

        return null;
    }

    protected Object getValueFromSlot(VirtualFrame frame, FrameSlot slot) {
        try {
            switch (slot.getKind()) {
                case Boolean:
                    return frame.getBoolean(slot);
                case Long:
                    return frame.getLong(slot);
                case Double:
                    return frame.getDouble(slot);
                case Byte:
                    return (char) frame.getByte(slot);
                case Object:
                    return frame.getObject(slot);
                default:
                    return null;
            }
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
    }

    protected void setValueToSlot(VirtualFrame frame, FrameSlot slot, Object value) throws FrameSlotTypeException {
        switch (slot.getKind()) {
            case Boolean:
                frame.setBoolean(slot, (boolean) value);
                break;
            case Long:
                frame.setLong(slot, (long) value);
                break;
            case Double:
                frame.setDouble(slot, (double) value);
                break;
            case Byte:
                frame.setByte(slot, (byte) (char) value);
                break;
            case Object:
                frame.setObject(slot, value);
                break;
        }
    }

}
