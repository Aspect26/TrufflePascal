package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant;

import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * Interface for type descriptors for constants.
 */
public interface ConstantDescriptor extends TypeDescriptor {

    /**
     * Gets the value of the constant/
     */
    Object getValue();

    /**
     * Checks whether this constant is signed (e.g.: integer vs. string).
     */
    boolean isSigned();

    /**
     * Gets the negated copy of this constant
     */
    ConstantDescriptor negatedCopy() throws LexicalException;

    /**
     * Gets the type of the constant.
     */
    TypeDescriptor getType();

}
