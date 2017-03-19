package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.NoBinaryRepresentationException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex.OrdinalDescriptor;

public class CharDescriptor extends PrimitiveDescriptor implements OrdinalDescriptor {

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Byte;
    }

    @Override
    public Object getDefaultValue() {
        return '\0';
    }

    @Override
    public int getSize() {
        return 256;
    }

    @Override
    public int getFirstIndex() {
        return Character.MIN_VALUE;
    }

    @Override
    public byte[] getBinaryRepresentation(Object value) {
        byte b = (byte) (char) value;
        return new byte[] { b };
    }

}