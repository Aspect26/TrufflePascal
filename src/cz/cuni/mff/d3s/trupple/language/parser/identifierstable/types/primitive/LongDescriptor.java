package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.NoBinaryRepresentationException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex.OrdinalDescriptor;

public class LongDescriptor extends PrimitiveDescriptor implements OrdinalDescriptor {

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Long;
    }

    @Override
    public Object getDefaultValue() {
        return 0L;
    }

    @Override
    public int getSize() {
        return Integer.SIZE;
    }

    @Override
    public int getFirstIndex() {
        return Integer.MIN_VALUE;
    }

    @Override
    public byte[] getBinaryRepresentation(Object value) {
        byte[] data = new byte[8];
        java.nio.ByteBuffer.wrap(data).putLong((long) value);
        return data;
    }

}