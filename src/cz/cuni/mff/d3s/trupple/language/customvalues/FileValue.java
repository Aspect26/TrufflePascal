package cz.cuni.mff.d3s.trupple.language.customvalues;

import java.io.File;

public class FileValue implements ICustomValue {

    private File file;
    private String filePath;

    @Override
    public Object getValue() {
        return this;
    }

    public void assignFilePath(String filePath) {
        this.filePath = filePath;
    }

}
