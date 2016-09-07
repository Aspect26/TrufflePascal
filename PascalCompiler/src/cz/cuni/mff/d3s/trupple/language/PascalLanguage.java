package cz.cuni.mff.d3s.trupple.language;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;

import cz.cuni.mff.d3s.trupple.language.parser.Parser;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

/*
 * QUSTIONS LIST 
 * how to use Graal
 * unit - support for initialization and finalization section ?? (v tp nie je, vo freepas je)
 *  
 * 
 * TODO LIST
 *
 * COMPILATION TIME TYPE CHECKING!!!!!!!!!!!!!!!!!!!!!! (assignment, operations, if condition, enum)
 * PREDAVANIE REFERENCIOU
 * GOTO (PROBLEM)
 * CONSTANTS
 * RECORD + WITH
 * TYPE SHORTCUT (e.g.: type i=integer; }
 * SETS
 * FILES + READ
 * POINTERS
 * CRT, GRAPH, STRING, DOS
 * 
 * ' in string 
 * private/global variables (mainly in unit)
 * break nie je v std (treba prepinac --std=turbo)
 * array v unite
 * enum v unite
 * array of array of array...
 * packed array
 * !assigning array
 * negative bound in array indexing
 * random + randomize
 *  
 * CHANGELOG:
 * v0.8
 * support readln
 * support multidimensional arrays
 * support nested subroutines
 * support subroutine forwarding
 * enums are now stored as objects in frame descriptors
 * break now throws ControlFlowException (instead of PascalRuntimeException) so Graal doesn't deoptimize the code
 * unit tests now use assertEquals instead of assertTrue
 * 
 * v0.7
 * support arrays
 * support enums
 * added JUnit tests
 * support not statement
 * else vetva v case
 * private/public methods in units
 * support parameterless subroutine calls without parenthesses
 * make for loop execute limiting expression only once
 * added args4j library
 * -I option syntax changed to gcc-like
 */

@TruffleLanguage.Registration(name = "Pascal", version = "0.7", mimeType = "text/x-pascal")
public final class PascalLanguage extends TruffleLanguage<PascalContext> {

	public static final PascalLanguage INSTANCE = new PascalLanguage();
	public static final String builtinKind = "Pascal builtin";

	@Override
	protected PascalContext createContext(com.oracle.truffle.api.TruffleLanguage.Env env) {
		return null;
	}

	@Override
	protected CallTarget parse(Source code, Node context, String... argumentNames) throws IOException {
		// final PascalContext _context = new PascalContext();
		// RootNode rootNode =_context.evalSource(code);
		// return Truffle.getRuntime().createCallTarget(rootNode);
		return null;
	}

	@Override
	protected Object findExportedSymbol(PascalContext context, String globalName, boolean onlyExplicit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getLanguageGlobal(PascalContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isObjectOfLanguage(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Object evalInContext(Source source, Node node, MaterializedFrame mFrame) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * *************************************************************************
	 * ******* START FROM FILE PATHS
	 */
	public static void start(String sourcePath, List<String> imports) throws IOException {
		Parser parser = new Parser();

		for (String dir : imports) {
			try{
				Files.walk(Paths.get(dir)).forEach(filePath -> {
					if(filePath.toString().endsWith(".pas")){
						try{
							parser.Parse(Source.fromFileName(filePath.toString()));
							if (!parser.noErrors()) {
								System.err.println("Errors while parsing import file " + filePath + ".");
								return;
							}
						} catch (IOException e){
						
						}
					}
				});
			} catch(NoSuchFileException e){
				System.err.println("No such file or directory " + e.getFile());
			}
		}
		
		parser.Parse(Source.fromFileName(sourcePath));
		if (!parser.noErrors()) {
			System.err.println("Errors while parsing source file, the code cannot be interpreted...");
			return;
		}

		Truffle.getRuntime().createCallTarget(parser.mainNode).call();
	}

	/*
	 * *************************************************************************
	 * START FROM CODES
	 */
	public static void startFromCodes(String sourceCode, List<String> imports, String codeDescription) {
		Parser parser = new Parser();

		int i = 0;
		for (String imp : imports) {
			parser.Parse(Source.fromText(imp, "import" + (i++)));
			if (!parser.noErrors()) {
				System.err.println("Errors while parsing import file " + imp + ".");
				return;
			}
		}

		parser.Parse(Source.fromText(sourceCode, codeDescription));
		if (!parser.noErrors()) {
			System.err.println("Errors while parsing source file, the code cannot be interpreted...");
			return;
		}

		Truffle.getRuntime().createCallTarget(parser.mainNode).call();
	}

	public Node createFindContextNode1() {
		return createFindContextNode();
	}

	public PascalContext findContext1(Node contextNode) {
		return findContext(contextNode);
	}
}
