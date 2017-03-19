package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.RouteTarget;

public class ReadVariableWithRouteNode extends ExpressionNode {

    @Child private AccessRouteNode accessRouteNode;
    private final FrameSlot firstSlot;

	public ReadVariableWithRouteNode(AccessRouteNode accessRouteNode, FrameSlot firstSlot) {
	    this.accessRouteNode = accessRouteNode;
        this.firstSlot = firstSlot;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
	    try {
	        accessRouteNode.executeVoid(frame);
            return accessRouteNode.getValue(frame);
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
    }

}
