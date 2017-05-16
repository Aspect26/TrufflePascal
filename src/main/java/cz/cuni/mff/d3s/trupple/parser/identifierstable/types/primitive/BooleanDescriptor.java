package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;

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

    @Override
    public TypeDescriptor getInnerTypeDescriptor() {
        return BooleanDescriptor.getInstance();
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return type instanceof PCharDesriptor;
    }

}