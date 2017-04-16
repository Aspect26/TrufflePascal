package cz.cuni.mff.d3s.trupple.language.customvalues;

public interface FileValue {

    Object read();

    void write(Object value);

    void write(Object[] values);

    void assignFilePath(String path);

    boolean eof();

    void openToRead();

    void openToWrite();

}
