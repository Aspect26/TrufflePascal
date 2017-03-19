package cz.cuni.mff.d3s.trupple.language.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.RecordValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;

import java.util.List;

// TODO: refactor this whole class
public abstract class AccessRouteNode extends StatementNode {

    abstract protected void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException;

    abstract protected void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException;

    void assign(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
        Object myValue = this.getValue(frame);
        if (myValue instanceof Reference) {
            this.assignReference((Reference) myValue, slotAssignment, value);
        } else {
            this.assignValue(frame, slotAssignment, value);
        }
    }

    abstract Object getValue(VirtualFrame frame) throws FrameSlotTypeException;

    public static class Simple extends AccessRouteNode {

        private final FrameSlot slot;

        public Simple(FrameSlot slot) {
            this.slot = slot;
        }

        @Override
        public void executeVoid(VirtualFrame frame) {

        }

        @Override
        protected void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
            slotAssignment.assign(frame, this.slot, value);
        }

        @Override
        protected void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
            slotAssignment.assign(reference.getFromFrame(), reference.getFrameSlot(), value);
        }

        @Override
        Object getValue(VirtualFrame frame) throws FrameSlotTypeException {
            return this.getValueFromSlot(frame, this.slot);
        }
    }

    public static class EnterRecord extends AccessRouteNode {

        @Child AccessRouteNode previousNode;
        private final String variableIdentifier;

        public EnterRecord(AccessRouteNode previousNode, String variableIdentifier) {
            this.previousNode = previousNode;
            this.variableIdentifier = variableIdentifier;
        }

        @Override
        public void executeVoid(VirtualFrame frame) {
            previousNode.executeVoid(frame);
        }

        @Override
        protected void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
            RecordValue record = (RecordValue) this.previousNode.getValue(frame);
            VirtualFrame recordsFrame = record.getFrame();
            FrameSlot targetSlot = this.findSlotByIdentifier(recordsFrame, this.variableIdentifier);
            slotAssignment.assign(recordsFrame, targetSlot, value);
        }

        @Override
        protected void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) {
            throw new PascalRuntimeException("Record element cannot be a reference.");
        }

        @Override
        Object getValue(VirtualFrame frame) throws FrameSlotTypeException {
            // TODO: this is a duplicity, check function above
            RecordValue record = (RecordValue) this.previousNode.getValue(frame);
            VirtualFrame recordsFrame = record.getFrame();
            FrameSlot targetSlot = this.findSlotByIdentifier(recordsFrame, this.variableIdentifier);

            return this.getValueFromSlot(recordsFrame, targetSlot);
        }
    }

    public static class ArrayIndex extends AccessRouteNode {

        @Child AccessRouteNode previousNode;
        @Children private final ExpressionNode[] indexExpressions;
        private Object[] indexes;

        public ArrayIndex(AccessRouteNode previousNode, List<ExpressionNode> indexExpressions) {
            this.previousNode = previousNode;
            this.indexExpressions = indexExpressions.toArray(new ExpressionNode[indexExpressions.size()]);
        }

        @Override
        public void executeVoid(VirtualFrame frame) {
            indexes = new Object[this.indexExpressions.length];

            for (int i = 0; i < this.indexExpressions.length; ++i) {
                indexes[i] = this.indexExpressions[i].executeGeneric(frame);
            }

            previousNode.executeVoid(frame);
        }

        @Override
        protected void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
            PascalArray array = (PascalArray) this.previousNode.getValue(frame);
            array.setValueAt(this.indexes, value);
        }

        @Override
        protected void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) {
            throw new PascalRuntimeException("Array element cannot be a reference.");
        }

        @Override
        Object getValue(VirtualFrame frame) throws FrameSlotTypeException {
            PascalArray array = (PascalArray) this.previousNode.getValue(frame);
            return array.getValueAt(this.indexes);
        }
    }

    public static class PointerDereference extends AccessRouteNode {

        @Child AccessRouteNode previousNode;

        public PointerDereference(AccessRouteNode previousNode) {
            this.previousNode = previousNode;
        }

        @Override
        public void executeVoid(VirtualFrame frame) {
            previousNode.executeVoid(frame);
        }

        @Override
        protected void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
            PointerValue pointer = (PointerValue) this.previousNode.getValue(frame);
            pointer.setDereferenceValue(value);
        }

        @Override
        protected void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) {
            throw new PascalRuntimeException("Dereference of a pointer cannot result into a reference variable.");
        }

        @Override
        Object getValue(VirtualFrame frame) throws FrameSlotTypeException {
            PointerValue pointer = (PointerValue) this.previousNode.getValue(frame);
            return pointer.getDereferenceValue();
        }
    }

}
