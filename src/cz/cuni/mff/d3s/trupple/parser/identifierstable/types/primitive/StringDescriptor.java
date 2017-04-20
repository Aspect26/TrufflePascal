package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;

public class StringDescriptor implements PrimitiveDescriptor {

    private static StringDescriptor instance = new StringDescriptor();

    public static StringDescriptor getInstance() {
        return instance;
    }

    private StringDescriptor() {

    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new PascalString();
    }

}