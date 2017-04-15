package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;

import java.io.Serializable;
import java.util.List;

public class EnumTypeDescriptor implements OrdinalDescriptor, Serializable {

    private final List<String> identifiers;
    private final String defaultValue;

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
        return (value instanceof String) && this.identifiers.contains(value);
    }

    @Override
    public int getFirstIndex() {
        return 0;
    }

    public List<String> getIdentifiers() {
        return this.identifiers;
    }

    public EnumValue getNext(String value) {
        int index = this.identifiers.indexOf(value);
        if (index == this.identifiers.size() - 1) {
            // TODO: throw a custom exception
            throw new IllegalArgumentException("No next element.");
        }

        return new EnumValue(this, this.identifiers.get(++index));
    }

    public EnumValue getPrevious(String value) {
        int index = this.identifiers.indexOf(value);
        if (index == 0) {
            // TODO: throw a custom exception
            throw new IllegalArgumentException("No previous element.");
        }

        return new EnumValue(this, this.identifiers.get(--index));
    }

    public long getOrdinalValue(String value) {
        return this.identifiers.indexOf(value);
    }
}
