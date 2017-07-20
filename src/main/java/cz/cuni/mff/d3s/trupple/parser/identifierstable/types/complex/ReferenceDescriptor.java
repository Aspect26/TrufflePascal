package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * Type descriptor for the reference passed variables. It contains additional information about the type of the variable
 * inside the reference.
 */
public class ReferenceDescriptor implements TypeDescriptor {

    private final TypeDescriptor typeOfReferenceDescriptor;

    public ReferenceDescriptor(TypeDescriptor typeOfReferenceDescriptor) {
        this.typeOfReferenceDescriptor = typeOfReferenceDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return this.typeOfReferenceDescriptor.convertibleTo(type);
    }

    public TypeDescriptor getReferencedType() {
        return typeOfReferenceDescriptor;
    }
}
