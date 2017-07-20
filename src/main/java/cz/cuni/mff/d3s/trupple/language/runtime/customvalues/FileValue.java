package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import com.oracle.truffle.api.CompilerDirectives;

/**
 * Interface for representations of variables of file types.
 */
@CompilerDirectives.ValueType
public interface FileValue {

    /**
     * Reads next value from the file and returns it.
     */
    Object read();

    /**
     * Stores one value inside the file.
     * @param value the value to be stored
     */
    void write(Object value);

    /**
     * Stores multiple values inside the file.
     * @param values the values to be stored
     */
    void write(Object[] values);

    /**
     * Stores multiple values inside the file and append a new line. Works only on textfile types.
     * @param values the values to be stored
     */
    void writeln(Object[] values);

    /**
     * Assigns a path to the file.
     * @param path the new file path
     */
    void assignFilePath(String path);

    /**
     * Checks whether we have reached the end of the file.
     */
    boolean eof();

    /**
     * Checks whether we have reached the end of the line.
     */
    boolean eoln();

    /**
     * Opens the file for reading.
     */
    void openToRead();

    /**
     * Opens the file for writting.
     */
    void openToWrite();

}
