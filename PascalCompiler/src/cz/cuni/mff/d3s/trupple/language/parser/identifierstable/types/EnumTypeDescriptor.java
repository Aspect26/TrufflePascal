package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

import java.util.List;

public class EnumTypeDescriptor extends TypeDescriptor {

    private List<String> identifiers;

    public EnumTypeDescriptor(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public boolean isVariable() {
        return false;
    }

}
