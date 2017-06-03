package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant;

import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public interface ConstantDescriptor extends TypeDescriptor {

    Object getValue();

    boolean isSigned();

    ConstantDescriptor negatedCopy() throws LexicalException;

    TypeDescriptor getType();

}
