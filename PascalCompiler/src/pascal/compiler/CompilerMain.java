package pascal.compiler;
import java.io.IOException;

import pascal.language.PascalLanguage;

public class CompilerMain {
	private static class Settings{
		public boolean verbose = false;
		public String sourcePath = "";
	}
	
	private static Settings settings = new Settings();
	
	public static void main(String[] args) throws IOException{
		if(args.length == 0){
			System.out.println("Please specify a source file to interpret.");
			return;
		}
		
		// process parameters
		for(int i=0; i<args.length - 1; i++){
			if(!processParameter(args[i])){
				System.out.println("Unknown parameter: " + args[i]);
				return;
			}
		}
		settings.sourcePath = args[args.length - 1];
		
		// start interpreter
		if(settings.verbose){
			System.out.println("Welcome to Trupple v0.4 made by \"Aspect\"");
			System.out.println("Starting interpretation...");
			System.out.println("----------------------------------");
		}
		
		PascalLanguage.start(settings.sourcePath);
		
		if(settings.verbose){
			System.out.println("----------------------------------");
			System.out.println("Interpretation finished...");
		}
	}
	
	private static boolean processParameter(String param){
		if (param.equals("-v"))
			settings.verbose = true;
		else 
			return false;
		
		return true;
	}
}
