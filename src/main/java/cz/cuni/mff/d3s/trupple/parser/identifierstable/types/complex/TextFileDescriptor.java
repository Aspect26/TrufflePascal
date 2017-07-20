package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex;

import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.TextFileValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * Specialized type descriptor for text-file values.
 */
public class TextFileDescriptor extends FileDescriptor {

    private TextFileDescriptor() {
        super(null);
    }

    private static TextFileDescriptor SINGLETON = new TextFileDescriptor();

    public static TextFileDescriptor getInstance() {
        return SINGLETON;
    }

    @Override
    public Object getDefaultValue() {
        return new TextFileValue();
    }

    @Override
    public boolean convertibleTo(TypeDescriptor typeDescriptor) {
        return super.convertibleTo(typeDescriptor) || typeDescriptor == getInstance();
    }

}
