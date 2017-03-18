package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.*;
import cz.cuni.mff.d3s.trupple.language.nodes.RouteTarget;

public abstract class AssignmentNodeWithRoute extends AssignmentNode {

    @Children private final AccessRouteNode[] accessRouteNodes;

    AssignmentNodeWithRoute(AccessRouteNode[] accessRouteNodes) {
        this.accessRouteNodes = accessRouteNodes;
    }

    @Override
    protected void makeAssignment(VirtualFrame frame, FrameSlot slot, SlotAssignment slotAssignment, Object value) {
        try {
            frame = this.getFrameContainingSlot(frame, slot);
            RouteTarget routeTarget = this.getRouteTarget(frame, slot, accessRouteNodes);
            if (routeTarget.isArray) {
                PascalArray array = (PascalArray) routeTarget.frame.getObject(routeTarget.slot);
                array.setValueAt(routeTarget.arrayIndexes, value);
            } else {
                slotAssignment.assign(routeTarget.frame, routeTarget.slot, value);
            }
        } catch (FrameSlotTypeException e) {
            throw new PascalRuntimeException("Wrong access");
        }
    }

}
