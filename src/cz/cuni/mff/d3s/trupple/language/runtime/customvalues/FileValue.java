package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;

@CompilerDirectives.ValueType
public interface FileValue {

    Object read();

    void write(Object value);

    void write(Object[] values);

    void writeln(Object[] values);

    void assignFilePath(String path);

    boolean eof();

    boolean eol();

    void openToRead();

    void openToWrite();

}
