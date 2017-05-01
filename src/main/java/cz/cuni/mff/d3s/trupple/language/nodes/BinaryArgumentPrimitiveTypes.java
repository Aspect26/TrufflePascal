package cz.cuni.mff.d3s.trupple.language.nodes;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import javafx.util.Pair;

public class BinaryArgumentPrimitiveTypes {

    private final Pair<TypeDescriptor, TypeDescriptor> types;

    public BinaryArgumentPrimitiveTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        this.types = new Pair<>(leftType, rightType);
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
