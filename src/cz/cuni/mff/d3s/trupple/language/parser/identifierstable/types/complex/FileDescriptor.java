package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

public class FileDescriptor implements TypeDescriptor {

    private final TypeDescriptor contentTypeDescriptor;

    public FileDescriptor(TypeDescriptor contentTypeDescriptor) {
        this.contentTypeDescriptor = contentTypeDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new FileValue();
    }

}
