package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.constant;

import cz.cuni.mff.d3s.trupple.language.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

public interface ConstantDescriptor extends TypeDescriptor {

    Object getValue();

    boolean isSigned();

    ConstantDescriptor negatedCopy() throws LexicalException;
}
