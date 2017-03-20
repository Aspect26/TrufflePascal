package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

public class SetDescriptor implements TypeDescriptor {

    private final OrdinalDescriptor baseTypeDescriptor;

    public SetDescriptor(OrdinalDescriptor baseTypeDescriptor) {
        this.baseTypeDescriptor = baseTypeDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new SetTypeValue();
    }

}
