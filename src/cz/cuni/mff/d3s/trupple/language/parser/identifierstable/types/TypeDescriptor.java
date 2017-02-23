package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.NotAnOrdinalException;

public abstract class TypeDescriptor {

    public abstract FrameSlotKind getSlotKind();

    /** Needed in assignment and reading single identifier **/
    // TODO: this should be called isValue maybe
    public abstract boolean isVariable();

    public OrdinalDescriptor getOrdinal() throws NotAnOrdinalException {
        throw new NotAnOrdinalException();
    }

    public Object getDefaultValue() {
        return null;
    }
}

