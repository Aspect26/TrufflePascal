package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.ObjectFileValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * Descriptor for a file type. Contains type descriptor of the type of file's content (e.g.: for  <i>file of integer</i>
 * it is type descriptor for the integer).
 */
public class FileDescriptor implements TypeDescriptor {

    private final TypeDescriptor contentTypeDescriptor;

    /**
     * Default constructor.
     * @param contentTypeDescriptor descriptor of the content type
     */
    public FileDescriptor(TypeDescriptor contentTypeDescriptor) {
        this.contentTypeDescriptor = contentTypeDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new ObjectFileValue();
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        FileDescriptor convertTo = (FileDescriptor) type;
        return (this.contentTypeDescriptor == null) || (convertTo.contentTypeDescriptor == null) ||
                (convertTo.contentTypeDescriptor == this.contentTypeDescriptor);
    }

}
