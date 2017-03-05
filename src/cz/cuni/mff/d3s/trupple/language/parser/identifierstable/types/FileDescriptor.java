package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;

public class FileDescriptor extends TypeDescriptor {

    private final TypeDescriptor contentTypeDescriptor;

    public FileDescriptor(TypeDescriptor contentTypeDescriptor) {
        this.contentTypeDescriptor = contentTypeDescriptor;
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
