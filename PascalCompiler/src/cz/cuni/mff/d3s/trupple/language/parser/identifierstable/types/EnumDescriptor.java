package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

import java.util.List;

public class EnumDescriptor extends TypeDescriptor {

    private List<String> identifiers;

    public EnumDescriptor(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }


}
