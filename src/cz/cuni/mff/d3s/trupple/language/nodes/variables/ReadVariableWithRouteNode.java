package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute.AccessNode;

public class ReadVariableWithRouteNode extends ExpressionNode {

    @Child private AccessNode accessNode;

	public ReadVariableWithRouteNode(AccessNode accessNode) {
	    this.accessNode = accessNode;
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
