package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customtypes.IOrdinalType;

import java.util.List;

public class ArrayDescriptor extends TypeDescriptor{

    private final List<IOrdinalType> ordinalDimenstions;
    private final TypeDescriptor returnTypeDescriptor;

    public ArrayDescriptor(List<IOrdinalType> dimensions, TypeDescriptor returnTypeDescriptor) {
        this.ordinalDimenstions = dimensions;
        this.returnTypeDescriptor = returnTypeDescriptor;
    }

    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }
}
