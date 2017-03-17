package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.constant;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.CantBeNegatedException;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;

public class BooleanConstantDescriptor implements OrdinalConstantDescriptor {

    private final boolean value;

    public BooleanConstantDescriptor(boolean value) {
        this.value = value;
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
        return (value)? 1 : 0;
    }

}
