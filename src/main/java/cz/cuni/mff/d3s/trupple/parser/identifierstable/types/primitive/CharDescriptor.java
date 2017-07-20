package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.CharConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.StringConstantDescriptor;

/**
 * Type descriptor representing the char type.
 */
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
    public TypeDescriptor getInnerTypeDescriptor() {
        return CharDescriptor.getInstance();
    }

    @Override
    public int getFirstIndex() {
        return Character.MIN_VALUE;
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return type == StringDescriptor.getInstance() || type instanceof CharConstantDescriptor ||
                type instanceof StringConstantDescriptor;
    }

}