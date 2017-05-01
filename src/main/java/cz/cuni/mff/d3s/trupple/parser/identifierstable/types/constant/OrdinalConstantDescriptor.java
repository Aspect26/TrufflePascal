package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public interface OrdinalConstantDescriptor extends ConstantDescriptor {

    int getOrdinalValue();

    TypeDescriptor getInnerType();

}
