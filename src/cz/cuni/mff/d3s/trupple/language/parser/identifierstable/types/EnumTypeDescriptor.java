package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

import java.util.List;

public class EnumTypeDescriptor implements OrdinalDescriptor {

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
    public String getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public int getSize() {
        return this.identifiers.size();
    }

    @Override
    public int getFirstIndex() {
        return 0;
    }

    public List<String> getIdentifiers() {
        return this.identifiers;
    }
}
