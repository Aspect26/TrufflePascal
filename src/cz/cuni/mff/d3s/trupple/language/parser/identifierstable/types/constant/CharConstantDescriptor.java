package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.constant;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.CantBeNegatedException;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;

public class CharConstantDescriptor implements OrdinalConstantDescriptor {

    private final char value;

    public CharConstantDescriptor(char value) {
        this.value = value;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Byte;
    }

    @Override
    public Object getDefaultValue() {
        return this.value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean isSigned() {
        return false;
    }

    @Override
    public ConstantDescriptor negatedCopy() throws LexicalException {
        throw new CantBeNegatedException();
    }

    @Override
    public int getOrdinalValue() {
        return (int)this.value;
    }

    @Override
    public byte[] getBinaryRepresentation(Object value) {
        byte b = (byte) (char) value;
        return new byte[] { b };
    }

}
