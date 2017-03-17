package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AccessRouteNode;

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
	
	boolean frameContainsSlot(VirtualFrame frame, FrameSlot slot) {
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

    /**
     * Find the VirtualFrame and its FrameSlot where the assignment target is located. The variables are found via
     * list of access route nodes. The result is stored in object's private properties slotsFrame and finalSlot. It also
     * sets the isArray field to true, if the assignment target shall be extracted from an array (the last access of the
     * variable is an array access, not record element access).
     * @param frame the frame, in which the array index expressions shall be evaluated
     * @return true if the target is an array, false otherwise
     * @throws FrameSlotTypeException if the access goes wrong (e.g.: it is indexing a variable which is not an array)
     */
    protected RouteTarget getRouteTarget(VirtualFrame frame, FrameSlot firstSlot, AccessRouteNode[] accessRouteNodes) throws FrameSlotTypeException {
        VirtualFrame slotsFrame = frame;
        FrameSlot finalSlot = firstSlot;
        boolean isArray = false;
        Object[] arrayIndexes = null;

        this.evaluateIndexNodes(frame, accessRouteNodes);
        RecordValue recordFromArray = null;

        for (AccessRouteNode accessRouteNode : accessRouteNodes) {
            if (accessRouteNode instanceof AccessRouteNode.EnterRecord) {
                RecordValue record = (recordFromArray == null)? (RecordValue) slotsFrame.getObject(finalSlot) : recordFromArray;
                slotsFrame = record.getFrame();
                String variableIdentifier = ((AccessRouteNode.EnterRecord) accessRouteNode).getVariableIdentifier();
                finalSlot = this.findSlotByIdentifier(slotsFrame, variableIdentifier);
                isArray = false;
                recordFromArray = null;
            } else if (accessRouteNode instanceof AccessRouteNode.ArrayIndex) {
                PascalArray array = (PascalArray) slotsFrame.getObject(finalSlot);
                arrayIndexes = ((AccessRouteNode.ArrayIndex) accessRouteNode).getIndexes();
                Object value = array.getValueAt(arrayIndexes);
                if (value instanceof RecordValue) {
                    recordFromArray = (RecordValue) value;
                }
                isArray = true;
            }
        }

        Reference referenceVariable = this.tryGetReference(slotsFrame, finalSlot);
        if (referenceVariable != null) {
            slotsFrame = referenceVariable.getFromFrame();
            finalSlot = referenceVariable.getFrameSlot();
        }

        return new RouteTarget(slotsFrame, finalSlot, isArray, arrayIndexes);
    }

    private void evaluateIndexNodes(VirtualFrame frame, AccessRouteNode[] accessRouteNodes) {
        for (AccessRouteNode accessRouteNode : accessRouteNodes) {
            accessRouteNode.executeVoid(frame);
        }
    }

    protected Reference tryGetReference(VirtualFrame frame, FrameSlot slot) {
        try {
            return (Reference)frame.getObject(slot);
        } catch (FrameSlotTypeException | ClassCastException e) {
            return null;
        }
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

}
