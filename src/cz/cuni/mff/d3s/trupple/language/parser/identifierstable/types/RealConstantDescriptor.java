package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class RealConstantDescriptor extends ConstantDescriptor{

    private final double value;

    public RealConstantDescriptor(double value) {
        this.value = value;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Long;
    }

    @Override
    public RealConstantDescriptor negatedCopy() {
        return new RealConstantDescriptor(-value);
    }

    public double getValue() {
        return this.value;
    }
}
