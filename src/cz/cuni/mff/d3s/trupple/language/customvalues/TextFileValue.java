package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.FileNotAssignedPathException;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.NotOpenedToReadException;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.NotOpenedToWriteException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class TextFileValue implements FileValue {

    private Scanner input;
    private PrintStream output;
    private String filePath;

    @Override
    public Object read() {
        if (this.input == null) {
            throw new NotOpenedToReadException();
        } else {
            return (char) this.input.nextByte();
        }
    }

    @Override
    public void write(Object value) {
        if (this.output == null) {
            throw new NotOpenedToWriteException();
        } else {
            this.output.print(value);
        }
    }

    @Override
    public void write(Object[] values) {
        for (Object value : values) {
            this.write(value);
        }
    }

    @Override
    public void assignFilePath(String path) {
        filePath = path;
    }

    @Override
    public boolean eof() {
        return !this.input.hasNext();
    }

    @Override
    public void openToRead() {
        if (this.filePath == null) {
            throw new FileNotAssignedPathException();
        }
        try {
            this.input = new Scanner(new File(this.filePath));
        } catch (FileNotFoundException e) {
            throw new cz.cuni.mff.d3s.trupple.language.runtime.exceptions.FileNotFoundException(this.filePath);
        }
    }

    @Override
    public void openToWrite() {
        if (this.filePath == null) {
            throw new FileNotAssignedPathException();
        }
        try {
            this.output = new PrintStream(new FileOutputStream(this.filePath));
        } catch (FileNotFoundException e) {
            throw new cz.cuni.mff.d3s.trupple.language.runtime.exceptions.FileNotFoundException(this.filePath);
        }
    }
}
