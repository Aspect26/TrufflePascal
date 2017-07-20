package cz.cuni.mff.d3s.trupple.main.exceptions;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.ByteArrayOutputStream;

/**
 * This exception is thrown when the interpreter is executed with wrong arguments or options. It provides the user an
 * informative help message about arguments and options.
 */
public class WrongOptionsException extends Exception {

    private String message;

    public WrongOptionsException(CmdLineException exception, CmdLineParser argsParser) {
        super("");
        this.message = exception.getMessage() + String.format("%n");
        this.message += "java -jar Trupple.jar [options...] sourcefile%n";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        argsParser.printUsage(baos);

        this.message += baos.toString();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
