package cz.cuni.mff.d3s.trupple.compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import cz.cuni.mff.d3s.trupple.language.PascalLanguage;

public class CompilerMain {
	
	private static class Settings {
		@Option(name="-v",usage="make compiler verbose")
	    public boolean verbose;
		
		@Option(name="-I",handler=ImportsOptionHandler.class,usage="specifies directories, where unit .tpa files are located")
	    public List<String> imports = new ArrayList<>();
		
		@Argument
		public List<String> arguments = new ArrayList<>();
		
		
		public String sourcePath;
	}
	
	public static void main(String[] args) throws IOException {
		new CompilerMain().start(args);
	}
	
	private void start(String[] args) throws IOException{
		
		Settings settings = new Settings();
		CmdLineParser parser = new CmdLineParser(settings);
		try {
			parser.parseArgument(args);
			
			if(settings.arguments == null || settings.arguments.size() == 0)
                throw new IOException("You must specify a source file.");
			// TODO: HIGH PRIORITY change this exception
			
			settings.sourcePath = settings.arguments.get(settings.arguments.size()-1);
		} catch(CmdLineException e) {
			System.err.println(e.getMessage());
            System.err.println("java -jar Trupple.jar [options...] sourcefile");
            parser.printUsage(System.err);
            System.err.println();
            return;
		}
		
		if (!(new File(settings.sourcePath).exists())) {
			System.err.println("The specified source doesn't exist: " + settings.sourcePath + ".");
			return;
		}

		// start interpreter
		if (settings.verbose) {
			System.out.println("Welcome to Trupple v0.8 made by \"Aspect\"");
			System.out.println("Starting interpretation...");
			System.out.println("----------------------------------");
		}

		PascalLanguage.start(settings.sourcePath, settings.imports);

		if (settings.verbose) {
			System.out.println("----------------------------------");
			System.out.println("Interpretation finished...");
		}
	}
}
