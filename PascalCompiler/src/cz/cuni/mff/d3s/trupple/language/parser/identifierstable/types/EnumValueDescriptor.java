package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class EnumValueDescriptor extends TypeDescriptor {

    private final EnumTypeDescriptor enumTypeDescriptor;
    private final String identifier;

    public EnumValueDescriptor(EnumTypeDescriptor enumTypeDescriptor, String identifier) {
        this.enumTypeDescriptor = enumTypeDescriptor;
        this.identifier = identifier;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public boolean isVariable() {
        return true;
    }

    public EnumTypeDescriptor getEnumTypeDescriptor() {
        return this.enumTypeDescriptor;
    }

}
