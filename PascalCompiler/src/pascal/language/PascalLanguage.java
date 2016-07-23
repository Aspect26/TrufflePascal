package pascal.language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;

import pascal.language.parser.Parser;
import pascal.language.runtime.PascalContext;

/**
 * TODO LIST
 *
 * ReadArgumentNode -> Object[] -> no specialization
 * ' in string
 * type check in assignment in parser
 * type check in all operations in parser
 * support for not
 * switch na error pokial sa neda vyhodnotit case
 * parser time check na break 
 * case bug - semicolon after last case option
 * float bug - exponent must have unary operator with it (+ or -)
 * 
 * --------- PLAN ------------------------------
 * --- PHASE 1 DONE
 * --- PHASE 2 DDONE
 * 
 * --- PHASE 3:
 * readln
 * read
 * 
 * --- PHASE 4:
 * goto
 * 
 * --- PHASE 5:
 * units
 * 
 * --- PHASE 6:
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

@TruffleLanguage.Registration(name = "Pascal", version = "0.5", mimeType = "text/x-pascal")
public final class PascalLanguage extends TruffleLanguage<PascalContext>{

	public static final PascalLanguage INSTANCE = new PascalLanguage();
	public static final String builtinKind = "Pascal builtin";
	
	@Override
	protected PascalContext createContext(com.oracle.truffle.api.TruffleLanguage.Env env) {
		return new PascalContext(
				env, 
				new BufferedReader(new InputStreamReader(env.in())),
				new PrintStream(env.out())
				);
	}
	
	@Override
	protected CallTarget parse(Source code, Node context, String... argumentNames) throws IOException {
		final PascalContext _context = new PascalContext();
		RootNode rootNode =_context.evalSource(code);
		return Truffle.getRuntime().createCallTarget(rootNode);
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
	
	public static void start(String sourcePath, List<String> imports) throws IOException {
		Parser parser = new Parser(new PascalContext(), Source.fromFileName(sourcePath));
		parser.Parse();
		if(!parser.noErrors()){
			System.out.println("Errors while parsing, the code cannot be interpreted...");
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
