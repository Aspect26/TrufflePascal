package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.ConstantDescriptor;

/**
 * Type descriptor for the <i>values</i> of an enum type. Contains additional information about the enum type to which
 * it belongs and the identifier of the enum value.
 */
public class EnumLiteralDescriptor implements TypeDescriptor, ConstantDescriptor {

    private final EnumTypeDescriptor enumTypeDescriptor;
    private final String identifier;

    /**
     * The default constructor.
     * @param enumTypeDescriptor descriptor of the enum to which this value belongs
     * @param identifier identifier of the enum value from a Pascal source
     */
    public EnumLiteralDescriptor(EnumTypeDescriptor enumTypeDescriptor, String identifier) {
        this.enumTypeDescriptor = enumTypeDescriptor;
        this.identifier = identifier;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new EnumValue(this.enumTypeDescriptor, this.identifier);
    }

    @Override
    public String toString() {
        return this.identifier;
    }

    @Override
    public Object getValue() {
        return new EnumValue(this.enumTypeDescriptor, this.identifier);
    }

    public EnumTypeDescriptor getEnumType() {
        return this.enumTypeDescriptor;
    }

    @Override
    public boolean isSigned() {
        return false;
    }

    @Override
    public ConstantDescriptor negatedCopy() throws LexicalException {
        return null;
    }

    @Override
    public TypeDescriptor getType() {
        return this;
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return type == this.enumTypeDescriptor ||
                type == GenericEnumTypeDescriptor.getInstance() ||
                ((type instanceof EnumLiteralDescriptor) && ((EnumLiteralDescriptor) type).getEnumType() == this.enumTypeDescriptor);
    }

}
