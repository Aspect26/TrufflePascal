package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public abstract class TypeDescriptor {

    public abstract FrameSlotKind getSlotKind();

    /** Needed in assignment and reading single identifier **/
    // TODO: this should be called isValue maybe
    public abstract boolean isVariable();

    public Object getDefaultValue() {
        return null;
    }
}

