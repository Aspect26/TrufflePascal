package cz.cuni.mff.d3s.trupple.language.nodes.utils;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class BinaryArgumentPrimitiveTypes {

    private final Tuple<TypeDescriptor, TypeDescriptor> types;

    public BinaryArgumentPrimitiveTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        this.types = new Tuple<>(leftType, rightType);
    }

    @Override
    public boolean equals(Object compareTo) {
        if (!(compareTo instanceof BinaryArgumentPrimitiveTypes)) {
            return false;
        }

        BinaryArgumentPrimitiveTypes compareToArgs = (BinaryArgumentPrimitiveTypes) compareTo;
        return compareToArgs.types.getKey().equals(this.types.getKey()) && compareToArgs.types.getValue().equals(this.types.getValue());
    }

    @Override
    public int hashCode() {
        return types.getKey().hashCode() * types.getValue().hashCode();
    }

    public TypeDescriptor getLeftType() {
        return this.types.getKey();
    }

    public TypeDescriptor getRightType() {
        return this.types.getValue();
    }

}
