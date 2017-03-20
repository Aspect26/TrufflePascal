package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute.AccessNode;

public class ReadVariableWithRouteNode extends ExpressionNode {

    @Child private AccessNode accessNode;
    private final FrameSlot firstSlot;

	public ReadVariableWithRouteNode(AccessNode accessNode, FrameSlot firstSlot) {
	    this.accessNode = accessNode;
        this.firstSlot = firstSlot;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
	    try {
	        accessNode.executeVoid(frame);
            return accessNode.getValue(frame);
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
    }

}
