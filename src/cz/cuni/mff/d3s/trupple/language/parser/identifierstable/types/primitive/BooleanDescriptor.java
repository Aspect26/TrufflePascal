package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex.OrdinalDescriptor;

public class BooleanDescriptor extends PrimitiveDescriptor implements OrdinalDescriptor {

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Boolean;
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public int getFirstIndex() {
        return 0;
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public byte[] getBinaryRepresentation(Object value) {
        byte[] data = new byte[1];
        data[0] = ((boolean) value)? (byte)1 : (byte)0;  // fuck java for no byte literal ...
        return data;
    }
}