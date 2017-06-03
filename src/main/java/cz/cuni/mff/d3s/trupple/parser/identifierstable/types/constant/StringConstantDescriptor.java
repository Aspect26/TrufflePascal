package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.parser.exceptions.CantBeNegatedException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.ArrayDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.StringDescriptor;

public class StringConstantDescriptor extends ArrayDescriptor implements ConstantDescriptor {

    private final PascalString value;

    public StringConstantDescriptor(String value) {
        super(new OrdinalDescriptor.RangeDescriptor(new LongConstantDescriptor(0), new LongConstantDescriptor(Integer.MAX_VALUE)), CharDescriptor.getInstance());
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
    public boolean convertibleTo(TypeDescriptor typeDescriptor) {
        return typeDescriptor == StringDescriptor.getInstance();
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public TypeDescriptor getType() {
        return StringDescriptor.getInstance();
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
