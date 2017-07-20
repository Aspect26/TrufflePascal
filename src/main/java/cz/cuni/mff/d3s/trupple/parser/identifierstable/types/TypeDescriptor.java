package cz.cuni.mff.d3s.trupple.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

/**
 * Interface for all type descriptors.
 */
public interface TypeDescriptor {

    /**
     * Gets the {@link FrameSlotKind} of the value that is represented by this descriptor.
     */
    FrameSlotKind getSlotKind();

    /**
     * Gets the default value of this type. It is used mainly for initialization of variables.
     */
    Object getDefaultValue();

    /**
     * Checks whether this type is convertible to the selected type.
     */
    boolean convertibleTo(TypeDescriptor typeDescriptor);

}

