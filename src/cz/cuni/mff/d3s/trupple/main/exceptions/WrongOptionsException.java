package cz.cuni.mff.d3s.trupple.main.exceptions;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.ByteArrayOutputStream;

public class WrongOptionsException extends CompilerException {

    public WrongOptionsException(CmdLineException exception, CmdLineParser argsParser) {
        super(ExitCodesEnum.EXIT_CODE_WRONG_OPTIONS);

        this.message = exception.getMessage() + String.format("%n");
        this.message += "java -jar Trupple.jar [options...] sourcefile%n";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        argsParser.printUsage(baos);

        this.message += baos.toString();
    }
}
