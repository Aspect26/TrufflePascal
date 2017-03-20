package cz.cuni.mff.d3s.trupple.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class UnknownDescriptor implements TypeDescriptor {

    public static UnknownDescriptor SINGLETON = new UnknownDescriptor();

    private UnknownDescriptor(){

    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Illegal;
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

}
