package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

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
}
