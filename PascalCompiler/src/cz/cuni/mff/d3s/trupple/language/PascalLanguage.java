package cz.cuni.mff.d3s.trupple.language;

import java.io.IOException;
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
 * QUSTIONS LIST how to use Graal integer range check (ako riesit unsigned ??)
 * 
 * TODO LIST
 *
 * ReadArgumentNode -> Object[] -> no specialization 
 * ' in string 
 * type check in assignment in parser 
 * type check in all operations in parser 
 * type check in if condition (is it boolean?) 
 * support for not switch na error pokial sa neda vyhodnotit case 
 * unit - can't have it's own "private" methods 
 * unit - variables declared only in IMPLEMENTATION section are visible from the outside 
 * unit - no support for initialization and finalization section
 * subroutines - support nested subroutines
 * parsovanie vstupnych parametrov -> kniznica nejaka
 * chyby do system.err 
 * niekde je SLNull 
 * break nie je v std (treba prepinac --std=turbo)
 * poriesit prepinac -I (nech funguje podobne ako v gcc (neimportuje subor ale dir, v ktorom ma hladat 
 * 	  kniznice importovane zo zdrojaku)
 * volanie subroutine bez zatvoriek pokial nema parametre
 * predavanie premennych referenciou
 * else vetva v case
 * 
 * --------- PLAN ------------------------------ 
 * --- PHASE 1 DONE
 * --- PHASE 2 DONE 
 * --- PHASE 5 DONE
 * 
 * --- PHASE 3: 
 * readln 
 * read
 * 
 * --- PHASE 4:
 * goto (later)
 * 
 * --- PHASE 6 -- CURRENT: 
 * enum 
 * array 
 * record
 * 
 * --- PHASE 7: 
 * files
 * 
 * --- PHASE 8: 
 * pointers
 * 
 * --- PHASE 9: 
 * crt 
 * graph 
 * string 
 * dos
 */

@TruffleLanguage.Registration(name = "Pascal", version = "0.6", mimeType = "text/x-pascal")
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
		PascalContext context = new PascalContext();
		Parser parser = new Parser(context);

		for (String imp : imports) {
			parser.Parse(Source.fromFileName(imp));
			if (!parser.noErrors()) {
				System.err.println("Errors while parsing import file " + imp + ".");
				return;
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
		PascalContext context = new PascalContext();
		Parser parser = new Parser(context);

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
