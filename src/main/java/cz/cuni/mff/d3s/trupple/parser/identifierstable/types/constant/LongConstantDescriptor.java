package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

/**
 * Type descriptor for a long-type constant. It also contains the constant's value.
 */
public class LongConstantDescriptor implements OrdinalConstantDescriptor {

    private final long value;

    /**
     * The default descriptor containing value of the constant.
     */
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
    public TypeDescriptor getType() {
        return LongDescriptor.getInstance();
    }

    @Override
    public TypeDescriptor getInnerType() {
        return this.getType();
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return (type instanceof LongDescriptor) || (type instanceof RealConstantDescriptor) || (type instanceof RealDescriptor);
    }

}
