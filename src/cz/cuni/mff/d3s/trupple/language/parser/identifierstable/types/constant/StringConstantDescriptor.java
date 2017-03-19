package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.constant;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.CantBeNegatedException;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;

public class StringConstantDescriptor implements ConstantDescriptor {

    private final String value;

    public StringConstantDescriptor(String value) {
        this.value = value;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
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
}
