package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public abstract class TypeDescriptor {

    public abstract FrameSlotKind getSlotKind();
}

