package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.NoBinaryRepresentationException;

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

    @Override
    public byte[] getBinaryRepresentation(Object value) {
        throw new NoBinaryRepresentationException();
    }

}
