package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class RealDescriptor implements PrimitiveDescriptor {

    private static RealDescriptor instance = new RealDescriptor();

    public static RealDescriptor getInstance() {
        return instance;
    }

    private RealDescriptor() {

    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Double;
    }

    @Override
    public Object getDefaultValue() {
        return 0.0d;
    }

}