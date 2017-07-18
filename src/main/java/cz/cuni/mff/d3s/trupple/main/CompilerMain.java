package cz.cuni.mff.d3s.trupple.main;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;
import cz.cuni.mff.d3s.trupple.main.exceptions.CompilerException;
import cz.cuni.mff.d3s.trupple.main.exceptions.WrongOptionsException;
import cz.cuni.mff.d3s.trupple.main.settings.Settings;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;

public class CompilerMain {

	private static Settings settings;

	public static void main(String[] args) {
        try {
            settings = parseArguments(args);
            PascalLanguage.INSTANCE.reset(settings.usesTPExtension(), settings.usesExtendedGoto());
            executeSource(Source.newBuilder(getSourceFile(settings.getSourcePath())).mimeType(PascalLanguage.MIME_TYPE).build(), System.in, System.out);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }

	}

	private static void executeSource(Source source, InputStream input, OutputStream output) throws Exception {
        PolyglotEngine engine = PolyglotEngine.newBuilder().setIn(input).setOut(output).setErr(System.err).build();
        assert engine.getLanguages().containsKey(PascalLanguage.MIME_TYPE);

        try {
            if (settings.usesTPExtension()) {
                UnitEvaluator.evalUnits(engine, settings.getIncludeDirectories());
            }
            engine.eval(source).execute();
        } catch (RuntimeException e) {
            System.err.print(e.getMessage().split(":")[1]);
        }
        engine.dispose();
    }

	private static Settings parseArguments(String[] args) throws CompilerException {
		Settings settings = new Settings();
		CmdLineParser argumentsParser = new CmdLineParser(settings);

		try {
			argumentsParser.parseArgument(args);
            return settings;
		} catch(CmdLineException e) {
			throw new WrongOptionsException(e, argumentsParser);
		}
	}

	private static File getSourceFile(String sourcePath) throws Exception {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            throw new Exception("Could not find: " + sourcePath);
        }
        return sourceFile;
    }

}
