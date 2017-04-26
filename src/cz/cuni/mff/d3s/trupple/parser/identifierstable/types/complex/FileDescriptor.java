package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.customvalues.ObjectFileValue;
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
            return new TextFileValue();
        } else {
            return new ObjectFileValue();
        }
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        FileDescriptor convertTo = (FileDescriptor) type;
        return (this.contentTypeDescriptor == null) || (convertTo.contentTypeDescriptor == null) ||
                (convertTo.contentTypeDescriptor == this.contentTypeDescriptor);
    }

}
