package cz.cuni.mff.d3s.trupple.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

// TODO: do we need this?
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

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return false;
    }

}
