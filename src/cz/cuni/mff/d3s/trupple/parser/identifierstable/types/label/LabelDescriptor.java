package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.label;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class LabelDescriptor implements TypeDescriptor {

    private final String identifier;

    public LabelDescriptor(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }
}
