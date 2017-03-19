package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;

import java.io.*;

public class FileValue implements ICustomValue {

    private BufferedOutputStream outputStream;
    private BufferedInputStream inputStream;
    private String filePath;

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

    private void verifyPathSet() {
        if (filePath == null) {
            // TODO: custom exception
            throw new PascalRuntimeException("No path assigned.");
        }
    }

}
