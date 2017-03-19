package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.exceptions.NotAnOrdinalException;

public interface TypeDescriptor {

    FrameSlotKind getSlotKind();

    Object getDefaultValue();

    byte[] getBinaryRepresentation(Object value);

}

