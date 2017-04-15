package cz.cuni.mff.d3s.trupple.main;

import java.io.File;
import java.io.IOException;
import cz.cuni.mff.d3s.trupple.main.exceptions.CompilerException;
import cz.cuni.mff.d3s.trupple.main.exceptions.SourceDoesntExistException;
import cz.cuni.mff.d3s.trupple.main.exceptions.WrongOptionsException;
import cz.cuni.mff.d3s.trupple.main.exceptions.WrongStandardException;
import cz.cuni.mff.d3s.trupple.main.settings.Settings;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;

public class CompilerMain {

	public static void main(String[] args) throws IOException {
		new CompilerMain().start(args);
	}

	private Settings settings;

	private static final String welcomeMessage = "" +
            "Welcome to Trupple v1.0 made by \"Aspect\"" +
            "Starting interpretation..." +
            "----------------------------------";

	private static final String exitMessage = "" +
            "----------------------------------" +
            "Interpretation finished...";
	
	private void start(String[] args) throws IOException {

	    if (!this.setSettings(args))
	        return;

		if (settings.verbose) {
			System.out.println(welcomeMessage);
		}

		PascalLanguage.start(settings.getSourcePath(), settings.getArguments(), settings.imports,
                settings.isTPExtensionSet(), settings.extendedGotoSupport);

		if (settings.verbose) {
			System.out.println(exitMessage);
		}
	}

	private boolean setSettings(String[] args) {
        try {
            this.settings = this.parseArguments(args);
            this.checkChosenStandard(settings.standard);
            this.assertSourceExists(settings.getSourcePath());
        } catch (CompilerException e) {
            System.err.print(e.getMessage());
            System.exit(e.getExitCode());
            return false;
        }

        return true;
    }

	private Settings parseArguments(String[] args) throws CompilerException {
		Settings settings = new Settings();
		CmdLineParser argumentsParser = new CmdLineParser(settings);

		try {
			argumentsParser.parseArgument(args);
			if(settings.getSourcePath() == null) {
                throw new IllegalArgumentException("You must specify a source file.");
            }
            return settings;
		} catch(CmdLineException e) {
			throw new WrongOptionsException(e, argumentsParser);
		}
	}

	private void checkChosenStandard(String standard) throws CompilerException {
	    if (!Settings.isStandard(standard)) {
	        throw new WrongStandardException(standard);
        }
    }

	private void assertSourceExists(String path) throws CompilerException {
        if (!(new File(path).exists())) {
            throw new SourceDoesntExistException(path);
        }
    }
}
