package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

public class PointerDescriptor implements TypeDescriptor {

    private final TypeDescriptor innerType;

    public PointerDescriptor(TypeDescriptor innerType) {
        this.innerType = innerType;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        // TODO: PointerValue.NILL
        return null;
    }
}
