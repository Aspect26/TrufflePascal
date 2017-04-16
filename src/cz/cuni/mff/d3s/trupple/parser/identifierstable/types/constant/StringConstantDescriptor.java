package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.parser.exceptions.CantBeNegatedException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;

public class StringConstantDescriptor implements ConstantDescriptor {

    private final PascalString value;

    public StringConstantDescriptor(String value) {
        this.value = new PascalString(value);
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
