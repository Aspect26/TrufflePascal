package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customtypes.IOrdinalType;

import java.util.List;

public class ArrayDescriptor extends TypeDescriptor {

    private final List<OrdinalDescriptor> ordinalDimenstions;
    private final TypeDescriptor returnTypeDescriptor;

    public ArrayDescriptor(List<OrdinalDescriptor> dimensions, TypeDescriptor returnTypeDescriptor) {
        this.ordinalDimenstions = dimensions;
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
}
