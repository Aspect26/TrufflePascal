package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class LongConstantDescriptor extends ConstantDescriptor{

    private final long value;

    public LongConstantDescriptor(long value) {
        this.value = value;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Long;
    }

    @Override
    public LongConstantDescriptor negatedCopy() {
        return new LongConstantDescriptor(-value);
    }

    public long getValue() {
        return this.value;
    }
}
