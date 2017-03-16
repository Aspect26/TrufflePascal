package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.RouteTarget;

public class ReadVariableWithRouteNode extends ExpressionNode {

    @Children private final AccessRouteNode[] accessRoute;
    private final FrameSlot firstSlot;

	public ReadVariableWithRouteNode(AccessRouteNode[] accessRoute, FrameSlot firstSlot) {
	    this.accessRoute = accessRoute;
        this.firstSlot = firstSlot;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
	    try {
            RouteTarget routeTarget = this.getRouteTarget(frame, firstSlot, accessRoute);
            if (routeTarget.isArray) {
                PascalArray array = (PascalArray) routeTarget.frame.getObject(routeTarget.slot);
                return array.getValueAt(routeTarget.arrayIndexes);
            } else {
                return this.getValueFromSlot(routeTarget.frame, routeTarget.slot);
            }
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Unexpected error");
        }
    }

}
