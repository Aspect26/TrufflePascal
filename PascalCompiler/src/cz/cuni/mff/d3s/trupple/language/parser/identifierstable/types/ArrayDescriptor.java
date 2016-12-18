package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

import java.util.List;

public class ArrayDescriptor extends TypeDescriptor {

    private final List<OrdinalDescriptor> ordinalDimensions;
    private final TypeDescriptor returnTypeDescriptor;

    public ArrayDescriptor(List<OrdinalDescriptor> dimensions, TypeDescriptor returnTypeDescriptor) {
        this.ordinalDimensions = dimensions;
        this.returnTypeDescriptor = returnTypeDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public boolean isVariable() {
        return true;
    }

    public List<OrdinalDescriptor> getDimensionDescriptors() {
        return this.ordinalDimensions;
    }

    public TypeDescriptor getReturnTypeDescriptor() {
        return this.returnTypeDescriptor;
    }
}
