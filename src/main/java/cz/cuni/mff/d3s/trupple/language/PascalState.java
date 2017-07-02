package cz.cuni.mff.d3s.trupple.language;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

public class PascalState {

    private Map<String, Object> globals;




    PascalState(PrintWriter output, InputStream input) {
        this.globals = new HashMap<>();
    }

    public void reset() {
        this.globals = new HashMap<>();
    }

    Object getExportedSymbol(String identifier) {
        return globals.get(identifier);
    }

}
