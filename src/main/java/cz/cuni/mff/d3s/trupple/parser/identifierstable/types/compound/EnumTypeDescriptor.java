package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;

import java.io.Serializable;
import java.util.List;

/**
 * Type descriptor for the enum <i>types</i>. It contains additional information about the values it contains and the
 * type's default value.
 */
public class EnumTypeDescriptor implements OrdinalDescriptor, Serializable {

    private final List<String> identifiers;
    // TODO: this is a bit useless overgeneralization since it is always the first declared value
    private final String defaultValue;

    /**
     * The default constructor.
     * @param identifiers list of identifiers of the enum type's values as they appear in a Pascal source
     */
    public EnumTypeDescriptor(List<String> identifiers) {
        this.identifiers = identifiers;
        this.defaultValue = identifiers.get(0);
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new EnumValue(this, this.defaultValue);
    }

    @Override
    public int getSize() {
        return this.identifiers.size();
    }

    @Override
    public boolean containsValue(Object value) {
        return (value instanceof EnumValue) && this.identifiers.contains(((EnumValue) value).getValue());
    }

    @Override
    public TypeDescriptor getInnerTypeDescriptor() {
        return this;
    }

    @Override
    public int getFirstIndex() {
        return 0;
    }

    public List<String> getIdentifiers() {
        return this.identifiers;
    }

    /**
     * Gets the next enum value of the specified value.
     */
    public EnumValue getNext(String value) {
        int index = this.identifiers.indexOf(value);
        if (index == this.identifiers.size() - 1) {
            throw new PascalRuntimeException("No next element.");
        }

        return new EnumValue(this, this.identifiers.get(++index));
    }

    /**
     * Gets the previous enum value of the specified value.
     */
    public EnumValue getPrevious(String value) {
        int index = this.identifiers.indexOf(value);
        if (index == 0) {
            throw new PascalRuntimeException("No previous element.");
        }

        return new EnumValue(this, this.identifiers.get(--index));
    }

    /**
     * Gets the ordinal value of the specified enum value (by its identifier as it appears in a Pascal source).
     */
    public long getOrdinalValue(String value) {
        return this.identifiers.indexOf(value);
    }

    public boolean convertibleTo(TypeDescriptor type) {
        return type == GenericEnumTypeDescriptor.getInstance();
    }

}
