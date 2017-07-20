package cz.cuni.mff.d3s.trupple.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

/**
 * Descriptor used for unknown types so the parser does no have to stop or throw exception when it encounters one.
 */
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
        return new Object();
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return false;
    }

}
