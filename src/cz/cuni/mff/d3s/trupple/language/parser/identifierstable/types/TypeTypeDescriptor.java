package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class TypeTypeDescriptor implements TypeDescriptor {

    private final TypeDescriptor typeDescriptor;

    public TypeTypeDescriptor(TypeDescriptor typeDescriptor) {
        this.typeDescriptor = typeDescriptor;
    }

    public TypeDescriptor getTypeDescriptor() {
        return this.typeDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return null;
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

}
