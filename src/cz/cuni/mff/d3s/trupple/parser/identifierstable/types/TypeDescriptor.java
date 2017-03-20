package cz.cuni.mff.d3s.trupple.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public interface TypeDescriptor {

    FrameSlotKind getSlotKind();

    Object getDefaultValue();

}

