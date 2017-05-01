package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

import java.util.Collections;

public class GenericEnumTypeDescriptor extends EnumTypeDescriptor {

    private static GenericEnumTypeDescriptor instance = new GenericEnumTypeDescriptor();

    public static GenericEnumTypeDescriptor getInstance() {
        return instance;
    }

    private GenericEnumTypeDescriptor() {
        super(Collections.singletonList("a"));
    }

    @Override
    public boolean convertibleTo(TypeDescriptor typeDescriptor) {
        return super.convertibleTo(typeDescriptor) || typeDescriptor instanceof EnumTypeDescriptor;
    }
}
