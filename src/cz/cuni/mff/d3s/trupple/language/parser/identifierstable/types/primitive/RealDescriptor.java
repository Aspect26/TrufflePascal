package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class RealDescriptor extends PrimitiveDescriptor {

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Double;
    }

    @Override
    public Object getDefaultValue() {
        return 0.0d;
    }

}