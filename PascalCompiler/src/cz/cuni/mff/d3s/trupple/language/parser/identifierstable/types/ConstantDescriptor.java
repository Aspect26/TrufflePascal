package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;

public abstract class ConstantDescriptor extends TypeDescriptor {

    @Override
    public boolean isVariable() {
        return false;
    }

    public ConstantDescriptor negatedCopy() throws LexicalException {
        throw new LexicalException("This constant type cannot be negated.");
    }

    public ConstantDescriptor shallowCopy() {
        try {
            return (ConstantDescriptor)this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
