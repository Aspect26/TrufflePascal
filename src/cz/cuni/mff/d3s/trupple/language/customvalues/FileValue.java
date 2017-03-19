package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

import java.io.*;

public class FileValue implements ICustomValue {

    private final TypeDescriptor typeOfFile;
    private BufferedOutputStream outputStream;
    private BufferedInputStream inputStream;
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
            this.outputStream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
        } catch (FileNotFoundException e) {
            // TODO: custom exception
            throw new PascalRuntimeException("Can't open file: " + this.filePath);
        }
    }

    public void openToRead() {
        this.verifyPathSet();

        try {
            this.inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
        } catch (FileNotFoundException e) {
            // TODO: custom exception
            throw new PascalRuntimeException("Can't open file: " + this.filePath);
        }
    }

    public void write(Object[] values) {
        this.verifyOpenToWrite();

        // TODO: type check (file against values)
        try {
            for (Object value : values) {
                byte[] valueData = this.typeOfFile.getBinaryRepresentation(value);
                this.outputStream.write(valueData);
            }
            this.outputStream.flush();
        } catch (IOException e) {
            // TODO: custom exception
            throw new PascalRuntimeException("Can't write to file.");
        }
    }

    public boolean isEof() {
        try {
            return this.inputStream.available() == 0;
        } catch (IOException e) {
            // TODO: custom exception
            throw new PascalRuntimeException("IOException");
        }
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
            throw new PascalRuntimeException("File not opened to write.");
        }
    }

}
