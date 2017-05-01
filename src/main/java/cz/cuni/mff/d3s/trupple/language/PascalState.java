package cz.cuni.mff.d3s.trupple.language;
import com.oracle.truffle.api.ExecutionContext;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PascalState extends ExecutionContext {

    private final PrintWriter output;
    private final Scanner input;
    private final Map<String, Object> globals;

    PascalState(PrintWriter output, InputStream input) {
        this.output = output;
        this.input = new Scanner(input);
        this.globals = new HashMap<>();
    }

    Object getExportedSymbol(String identifier) {
        return globals.get(identifier);
    }

    public Scanner getInput() {
        return this.input;
    }

}
