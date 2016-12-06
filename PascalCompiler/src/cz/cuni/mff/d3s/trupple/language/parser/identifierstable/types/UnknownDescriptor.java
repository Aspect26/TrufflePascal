package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class UnknownDescriptor extends TypeDescriptor {

    public static UnknownDescriptor SINGLETON = new UnknownDescriptor();

    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Illegal;
    }

    private UnknownDescriptor(){

    }
}
