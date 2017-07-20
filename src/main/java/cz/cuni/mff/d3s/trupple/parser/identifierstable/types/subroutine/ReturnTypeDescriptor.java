package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * Type descriptor representing the variables that server as return values of functions.
 */
public class ReturnTypeDescriptor implements TypeDescriptor {

    private final TypeDescriptor returnTypeDescriptor;

    public ReturnTypeDescriptor(TypeDescriptor returnTypeDescriptor) {
        this.returnTypeDescriptor = returnTypeDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return returnTypeDescriptor.getSlotKind();
    }

    @Override
    public Object getDefaultValue() {
        return this.returnTypeDescriptor.getDefaultValue();
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return false;
    }

}
