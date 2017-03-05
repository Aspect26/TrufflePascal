package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;

public interface ConstantDescriptor extends TypeDescriptor {

    Object getValue();

    boolean isSigned();

    ConstantDescriptor negatedCopy() throws LexicalException;
}
