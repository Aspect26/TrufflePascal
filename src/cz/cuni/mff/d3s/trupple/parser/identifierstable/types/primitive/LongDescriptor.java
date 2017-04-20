package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;

public class LongDescriptor implements OrdinalDescriptor, PrimitiveDescriptor {

    private static LongDescriptor instance = new LongDescriptor();

    public static LongDescriptor getInstance() {
        return instance;
    }

    private LongDescriptor() {

    }

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
    public boolean containsValue(Object value) {
        return value instanceof Integer || value instanceof Long;
    }

    @Override
    public int getFirstIndex() {
        return Integer.MIN_VALUE;
    }

}