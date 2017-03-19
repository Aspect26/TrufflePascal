package cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute;

import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;


public abstract class AccessNode extends StatementNode {

    protected abstract void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException;

    protected abstract void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException;

    public void assign(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
        Object myValue = this.getValue(frame);
        if (myValue instanceof Reference) {
            this.assignReference((Reference) myValue, slotAssignment, value);
        } else {
            this.assignValue(frame, slotAssignment, value);
        }
    }

    public abstract Object getValue(VirtualFrame frame) throws FrameSlotTypeException;

}
