package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.EndOfFileException;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.FileNotAssignedPathException;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.NotOpenedToReadException;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.NotOpenedToWriteException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TextFileValue implements FileValue {

    private Scanner input;
    private Scanner inputLine;
    private PrintStream output;
    private String filePath;

    @Override
    public Object read() {
        if (this.input == null) {
            throw new NotOpenedToReadException();
        } else if (this.inputLine == null) {
            throw new EndOfFileException();
        } else if (!this.inputLine.hasNext()) {
            this.bufferLine();
        }

        if (this.inputLine == null) {
            throw new EndOfFileException();
        } else {
            return this.inputLine.next().charAt(0);
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
    public void writeln(Object[] values) {
        for (Object value : values) {
            this.write(value);
        }
        this.write(String.format("%n"));
    }

    @Override
    public void assignFilePath(String path) {
        filePath = path;
    }

    @Override
    public boolean eof() {
        if (this.inputLine == null) {
            return true;
        }
        if (this.inputLine.hasNext()) {
            return false;
        } else {
            this.bufferLine();
            return this.inputLine == null;
        }
    }

    @Override
    public boolean eoln() {
        return inputLine == null || !this.inputLine.hasNext();
    }

    @Override
    public void openToRead() {
        if (this.filePath == null) {
            throw new FileNotAssignedPathException();
        }
        try {
            this.input = new Scanner(new File(this.filePath));
            this.input.useDelimiter("(?<=.)");
            this.bufferLine(false);
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

    private void bufferLine() {
        this.bufferLine(true);
    }

    private void bufferLine(boolean prependNewline) {
        try {
            String newLine = this.input.nextLine();
            if (prependNewline) {
                newLine = String.format("%n" + newLine);
            }
            this.inputLine = new Scanner(newLine);
            this.inputLine.useDelimiter("");
        } catch (NoSuchElementException e) {
            this.inputLine = null;
        }
    }
}
