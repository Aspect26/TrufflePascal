package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class SetDescriptor implements TypeDescriptor {

    private final OrdinalDescriptor baseTypeDescriptor;

    public SetDescriptor(OrdinalDescriptor baseTypeDescriptor) {
        this.baseTypeDescriptor = baseTypeDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    public OrdinalDescriptor getBaseTypeDescriptor() {
        return this.baseTypeDescriptor;
    }
}
