package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileValue implements ICustomValue {

    private BufferedOutputStream outputStream;
    private String filePath;

    @Override
    public Object getValue() {
        return this;
    }

    public void assignFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void openToWrite() {
        if (filePath == null) {
            // TODO: custom exception
            throw new PascalRuntimeException("No path assigned.");
        }

        try {
            this.outputStream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
        } catch (FileNotFoundException e) {
            // TODO: custom exception
            throw new PascalRuntimeException("Can't open file: " + this.filePath);
        }
    }

}
