package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.ConstantDescriptor;

public class EnumLiteralDescriptor implements TypeDescriptor, ConstantDescriptor {

    private final EnumTypeDescriptor enumTypeDescriptor;
    private final String identifier;

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
        return this.identifier;
    }

    @Override
    public boolean isSigned() {
        return false;
    }

    @Override
    public ConstantDescriptor negatedCopy() throws LexicalException {
        return null;
    }
}
