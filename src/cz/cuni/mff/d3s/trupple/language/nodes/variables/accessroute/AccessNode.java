package cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute;

import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;


public abstract class AccessNode extends StatementNode {

    @Child AccessNode applyToNode;

    AccessNode(AccessNode applyToNode) {
        this.applyToNode = applyToNode;
    }

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

    protected abstract Object applyTo(VirtualFrame frame, Object value);

    public final Object getValue(VirtualFrame frame) throws FrameSlotTypeException {
        if (this.applyToNode == null) {
            return this.applyTo(frame, null);
        }

        Object value = this.applyToNode.getValue(frame);

        if (value instanceof Reference) {
            Reference reference = (Reference) value;
            Object referenceObject = reference.getFromFrame().getObject(reference.getFrameSlot());
            return this.applyTo(frame, referenceObject);
        } else {
            return this.applyTo(frame, value);
        }
    }

}
