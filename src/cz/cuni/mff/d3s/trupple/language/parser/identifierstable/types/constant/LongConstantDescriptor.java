package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.constant;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.NoBinaryRepresentationException;

public class LongConstantDescriptor implements OrdinalConstantDescriptor {

    private final long value;

    public LongConstantDescriptor(long value) {
        this.value = value;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Long;
    }

    @Override
    public Object getDefaultValue() {
        return value;
    }

    @Override
    public LongConstantDescriptor negatedCopy() {
        return new LongConstantDescriptor(-value);
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean isSigned() {
        return true;
    }

    @Override
    public int getOrdinalValue() {
        return (int)this.value;
    }

    @Override
    public byte[] getBinaryRepresentation(Object value) {
        byte[] data = new byte[8];
        java.nio.ByteBuffer.wrap(data).putLong((long) value);
        return data;
    }
}
