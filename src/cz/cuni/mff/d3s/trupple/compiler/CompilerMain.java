package cz.cuni.mff.d3s.trupple.compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.trupple.language.parser.IParser;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;

public class CompilerMain {
	
	private static class Settings {

	    public static final String STANDARD_WIRTH = "wirth";
	    public static final String STANDARD_TP = "turbo";

		@Option(name="-v", usage="make compiler verbose")
	    public boolean verbose;
		
		@Option(name="-I", handler=ImportsOptionHandler.class, usage="specifies directories, where unit .tpa files are located")
	    public List<String> imports = new ArrayList<>();

		// TODO: crete custom handler for this
		@Option(name="-std", usage="sets the stanadrd to be used")
        public String standard = STANDARD_WIRTH;
		
		@Argument
		public List<String> arguments = new ArrayList<>();
		
		
		public String sourcePath;
	}
	
	public static void main(String[] args) throws IOException {
		new CompilerMain().start(args);
	}
	
	private void start(String[] args) throws IOException{
		
		Settings settings = new Settings();
		CmdLineParser argumentsParser = new CmdLineParser(settings);
		try {
			argumentsParser.parseArgument(args);
			
			if(settings.arguments == null || settings.arguments.size() == 0)
                throw new IOException("You must specify a source file.");
			// TODO: HIGH PRIORITY change this exception
			
			settings.sourcePath = settings.arguments.get(settings.arguments.size()-1);
		} catch(CmdLineException e) {
			System.err.println(e.getMessage());
            System.err.println("java -jar Trupple.jar [options...] sourcefile");
            argumentsParser.printUsage(System.err);
            System.err.println();
            return;
		}
		
		if (!(new File(settings.sourcePath).exists())) {
			System.err.println("The specified source doesn't exist: " + settings.sourcePath + ".");
			return;
		}

		if (!settings.standard .equals(Settings.STANDARD_TP) && !settings.standard.equals(Settings.STANDARD_WIRTH)) {
			System.err.println("Unknown stanard specified: " + settings.standard + ". Please use '" + Settings.STANDARD_TP + "' or '" + Settings.STANDARD_WIRTH + "'");
		}

		// start interpreter
		if (settings.verbose) {
			System.out.println("Welcome to Trupple v0.9 made by \"Aspect\"");
			System.out.println("Starting interpretation...");
			System.out.println("----------------------------------");
		}

		PascalLanguage.start(settings.sourcePath, settings.imports, settings.standard.equals(Settings.STANDARD_TP));

		if (settings.verbose) {
			System.out.println("----------------------------------");
			System.out.println("Interpretation finished...");
		}
	}
}
