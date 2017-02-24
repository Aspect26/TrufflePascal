package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.NotAnOrdinalException;

public class ReferenceDescriptor extends TypeDescriptor {

    private final TypeDescriptor typeOfReferenceDescriptor;

    public ReferenceDescriptor(TypeDescriptor typeOfReferenceDescriptor) {
        this.typeOfReferenceDescriptor = typeOfReferenceDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public boolean isVariable() {
        return this.typeOfReferenceDescriptor.isVariable();
    }

    @Override
    public OrdinalDescriptor getOrdinal() throws NotAnOrdinalException {
        return this.typeOfReferenceDescriptor.getOrdinal();
    }

    @Override
    public Object getDefaultValue() {
        return this.typeOfReferenceDescriptor.getDefaultValue();
    }
}
