package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;

public class BooleanDescriptor implements OrdinalDescriptor, PrimitiveDescriptor {

    private static BooleanDescriptor instance = new BooleanDescriptor();

    public static BooleanDescriptor getInstance() {
        return instance;
    }

    private BooleanDescriptor() {

    }

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
    public boolean containsValue(Object value) {
        return value instanceof Boolean;
    }

}