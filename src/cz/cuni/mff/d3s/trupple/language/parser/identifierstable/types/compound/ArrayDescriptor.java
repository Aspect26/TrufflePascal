package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.compound;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

import java.util.List;

public class ArrayDescriptor implements TypeDescriptor {

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
    public Object getDefaultValue() {
        return null;
    }

    public List<OrdinalDescriptor> getDimensionDescriptors() {
        return this.ordinalDimensions;
    }

    public TypeDescriptor getReturnTypeDescriptor() {
        return this.returnTypeDescriptor;
    }
}
