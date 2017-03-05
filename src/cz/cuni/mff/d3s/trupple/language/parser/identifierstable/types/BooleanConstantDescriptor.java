package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class BooleanConstantDescriptor extends ConstantDescriptor{

    private final boolean value;

    public BooleanConstantDescriptor(boolean value) {
        this.value = value;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Long;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean isSigned() {
        return false;
    }
}
