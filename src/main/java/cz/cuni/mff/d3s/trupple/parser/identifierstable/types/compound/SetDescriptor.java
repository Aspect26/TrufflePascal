package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * Type descriptor for Pascal's set types. It contains additional information about the universe from which it can
 * contains its values.
 */
public class SetDescriptor implements TypeDescriptor {

    private final OrdinalDescriptor baseTypeDescriptor;

    /**
     * The default constructor.
     * @param baseTypeDescriptor universe of values it can contain
     */
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

    /**
     * Gets the type descriptor of the universe of the values this set type can contain.
     */
    public OrdinalDescriptor getBaseTypeDescriptor() {
        return this.baseTypeDescriptor;
    }

    /**
     * Gets the type descriptor of the values this set can contain.
     */
    public TypeDescriptor getInnerType() {
        return baseTypeDescriptor.getInnerTypeDescriptor();
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return false;
    }

}
