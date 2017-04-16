package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.PrimitiveFileValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.TextFileValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;

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
        if (contentTypeDescriptor instanceof CharDescriptor) {
            return new TextFileValue(this.contentTypeDescriptor);
        } else {
            return new PrimitiveFileValue(this.contentTypeDescriptor);
        }
    }

}
