package pascal.compiler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pascal.language.PascalLanguage;

public class CompilerMain {
	private static class Settings{
		public boolean verbose = false;
		public String sourcePath = "";
		public List<String> imports = new ArrayList<>();
	}
	
	private static class ArgumentsScanner{
		private final String[] args;
		private int currentIndex;
		
		public ArgumentsScanner(String[] args){
			this.args = args;
			this.currentIndex = 0;
		}
		
		public String getNext(boolean param){
			if(currentIndex >= args.length - 1)
				return null;
			
			if(!param)
				return args[currentIndex++];
			
			String arg = args[currentIndex++];
			if(arg.charAt(0) != '-')
				return null;
			
			return arg.substring(1);
		}
		
		public boolean isNextArgument(){
			if (currentIndex + 1 >= args.length-1)
				return false;
			
			return args[currentIndex + 1].charAt(0) != '-';
		}
		
		public String getSourcePath(){
			return args[args.length-1];
		}
	}
	
	private static Settings settings = new Settings();
	
	public static void main(String[] args) throws IOException{
		if(args.length == 0){
			System.out.println("Please specify a source file to interpret.");
			return;
		}
		
		// process parameters
		if(!processParameters(args)){
			System.out.println("Error processing arguments. The interpreter will close now.");
			return;
		}
		
		if(! (new File(settings.sourcePath).exists())){
			System.out.println("The specified source doesn't exist: " + settings.sourcePath + ".");
			return;
		}
		
		// start interpreter
		if(settings.verbose){
			System.out.println("Welcome to Trupple v0.6 made by \"Aspect\"");
			System.out.println("Starting interpretation...");
			System.out.println("----------------------------------");
		}
		
		PascalLanguage.start(settings.sourcePath, settings.imports);
		
		if(settings.verbose){
			System.out.println("----------------------------------");
			System.out.println("Interpretation finished...");
		}
	}
	
	private static boolean processParameters(String[] params){
		ArgumentsScanner scanner = new ArgumentsScanner(params);
		
		String arg;
		while((arg = scanner.getNext(true)) != null){
			if(!processParameter(arg, scanner))
				return false;
		}
		
		settings.sourcePath = scanner.getSourcePath();
		return true;
	}
	
	private static boolean processParameter(String param, ArgumentsScanner scanner){
		if (param.equals("v"))
			settings.verbose = true;
		else if(param.equals("i")){
			while(scanner.isNextArgument()){
				String arg = scanner.getNext(false);
				if(arg == null){
					System.out.println("Wrong argument given for parameter " + param);
					return false;
				}
			
				settings.imports.add(arg);
			}
		}
		else{
			System.out.println("Unknown parameter " + param);
			return false;
		}
		
		return true;
	}
}
