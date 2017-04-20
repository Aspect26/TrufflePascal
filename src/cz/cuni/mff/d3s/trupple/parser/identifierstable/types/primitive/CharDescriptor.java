package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;

public class CharDescriptor implements PrimitiveDescriptor, OrdinalDescriptor {

    private static CharDescriptor instance = new CharDescriptor();

    public static CharDescriptor getInstance() {
        return instance;
    }

    private CharDescriptor() {

    }

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
    public boolean containsValue(Object value) {
        return value instanceof Character;
    }

    @Override
    public int getFirstIndex() {
        return Character.MIN_VALUE;
    }

}