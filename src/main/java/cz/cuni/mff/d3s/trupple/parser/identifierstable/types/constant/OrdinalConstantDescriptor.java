package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * Specialized interface for the ordinal constants.
 */
public interface OrdinalConstantDescriptor extends ConstantDescriptor {

    /**
     * Gets the ordinal value of the constant.
     */
    int getOrdinalValue();

    /**
     * Gets the type of the constant.
     * @deprecated because the {@link ConstantDescriptor} already contains this function.
     */
    @Deprecated
    TypeDescriptor getInnerType();

}
