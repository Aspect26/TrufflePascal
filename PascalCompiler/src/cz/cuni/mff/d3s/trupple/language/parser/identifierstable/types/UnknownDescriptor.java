package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class UnknownDescriptor extends TypeDescriptor {

    public static UnknownDescriptor SINGLETON = new UnknownDescriptor();

    private UnknownDescriptor(){

    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Illegal;
    }

    @Override
    public boolean isVariable() {
        return false;
    }
}
