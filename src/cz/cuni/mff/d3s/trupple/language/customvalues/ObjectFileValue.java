package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.*;

import java.io.*;
import java.io.FileNotFoundException;

public class ObjectFileValue implements FileValue {

    private PushbackInputStream pushbackInput;
    private ObjectInputStream objectInput;
    private ObjectOutputStream output;
    private String filePath;

    @Override
    public Object read() {
        try {
            return this.objectInput.readObject();
        } catch (IOException e) {
            throw new PascalRuntimeException("Unexpected exception thrown while reading from a file");
        } catch (ClassNotFoundException e) {
            throw new PascalRuntimeException("Wrong data in an input file");
        }
    }

    @Override
    public void write(Object value) {
        try {
            this.output.writeObject(value);
        } catch (IOException e) {
            throw new PascalRuntimeException("Unexpected exception thrown while writing to a file");
        }
    }

    @Override
    public void write(Object[] values) {
        for (Object value : values) {
            this.write(value);
        }
    }

    @Override
    public void assignFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean eof() {
        try {
            int result = this.pushbackInput.read();
            if (result != -1) {
                this.pushbackInput.unread(result);
                return false;
            }

            return true;
        } catch (IOException e) {
            throw new PascalRuntimeException("Unexpected exception thrown while reading a file");
        }
    }

    @Override
    public boolean eol() {
        return false;
    }

    @Override
    public void openToRead() {
        if (this.filePath == null) {
            throw new FileNotAssignedPathException();
        }
        try {
            this.pushbackInput = new PushbackInputStream(new FileInputStream(this.filePath));
            this.objectInput = new ObjectInputStream(this.pushbackInput);
        } catch (FileNotFoundException e) {
            throw new cz.cuni.mff.d3s.trupple.language.runtime.exceptions.FileNotFoundException(this.filePath);
        } catch (IOException e) {
            throw new PascalRuntimeException("Unexpected exception while opening a file");
        }
    }

    @Override
    public void openToWrite() {
        if (this.filePath == null) {
            throw new FileNotAssignedPathException();
        }
        try {
            this.output = new ObjectOutputStream(new FileOutputStream(this.filePath));
        } catch (FileNotFoundException e) {
            throw new cz.cuni.mff.d3s.trupple.language.runtime.exceptions.FileNotFoundException(this.filePath);
        } catch (IOException e) {
            throw new PascalRuntimeException("Unexpected exception while opening a file");
        }
    }
}
