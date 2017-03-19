package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

import java.io.*;

public class FileValue implements ICustomValue {

    private final TypeDescriptor typeOfFile;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String filePath;

    public FileValue(TypeDescriptor typeOfFile) {
        this.typeOfFile = typeOfFile;
    }

    @Override
    public Object getValue() {
        return this;
    }

    public void assignFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void openToWrite() {
        this.verifyPathSet();

        try {
            this.outputStream = new ObjectOutputStream(new FileOutputStream(new File(filePath)));
        } catch (IOException e) {
            // TODO: custom exception
            throw new PascalRuntimeException("Can't open file: " + this.filePath);
        }
    }

    public void openToRead() {
        this.verifyPathSet();

        try {
            this.inputStream = new ObjectInputStream(new FileInputStream(new File(filePath)));
        } catch (IOException e) {
            // TODO: custom exception
            throw new PascalRuntimeException("Can't open file: " + this.filePath);
        }
    }

    public void write(Object[] values) {
        this.verifyOpenToWrite();

        // TODO: type check (file against values)
        try {
            for (Object value : values) {
                this.outputStream.writeObject(value);
            }
            this.outputStream.flush();
        } catch (IOException e) {
            // TODO: custom exception
            throw new PascalRuntimeException("Can't write to file.");
        }
    }

    public Object read() {
        this.verifyOpenToRead();

        try {
            return this.inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new PascalRuntimeException("Unknown data in a file.");
        } catch (IOException e) {
            // TODO: custom exception
            throw new PascalRuntimeException("Can't read from a file.");
        }
    }

    public boolean isEof() {
        this.verifyOpenToRead();

        // TODO: java streams doesnt have eof() function? fuck java
        return false;
    }

    private void verifyPathSet() {
        if (filePath == null) {
            // TODO: custom exception
            throw new PascalRuntimeException("No path assigned.");
        }
    }

    private void verifyOpenToWrite() {
        if (outputStream == null) {
            // TODO: custom exception
            throw new PascalRuntimeException("File not opened to write.");
        }
    }

    private void verifyOpenToRead() {
        if (inputStream == null) {
            // TODO: custom exception
            throw new PascalRuntimeException("File not opened to read.");
        }
    }

}
