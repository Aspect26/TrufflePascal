package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class RealConstantDescriptor implements ConstantDescriptor {

    private final double value;

    public RealConstantDescriptor(double value) {
        this.value = value;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Long;
    }

    @Override
    public Object getDefaultValue() {
        return 0d;
    }

    @Override
    public RealConstantDescriptor negatedCopy() {
        return new RealConstantDescriptor(-value);
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean isSigned() {
        return true;
    }
}
