package cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute;

import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class PointerDereference extends AccessNode {

    private final TypeDescriptor dereferenceType;

    public PointerDereference(AccessNode applyToNode, TypeDescriptor dereferenceType) {
        super(applyToNode);
        this.dereferenceType = dereferenceType;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        this.applyToNode.executeVoid(frame);
        return null;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        this.applyToNode.executeVoid(frame);
    }

    @Override
    public TypeDescriptor getType() {
        return this.dereferenceType;
    }

    @Override
    protected void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
        PointerValue pointer = (PointerValue) this.applyToNode.getValue(frame);
        pointer.setDereferenceValue(value);
    }

    @Override
    protected void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) {
        throw new PascalRuntimeException("Dereference of a pointer cannot result into a reference variable.");
    }

    @Override
    protected Object applyTo(VirtualFrame frame, Object value) {
        if (value instanceof PointerValue) {
            PointerValue pointer = (PointerValue) value;
            return pointer.getDereferenceValue();
        } else {
            throw new PascalRuntimeException("Can't dereference this type.");
        }

    }

}