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

import cz.cuni.mff.d3s.trupple.language.parser.IParser;
import cz.cuni.mff.d3s.trupple.language.parser.wirth.Parser;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@TruffleLanguage.Registration(name = "Pascal", version = "0.8", mimeType = "text/x-pascal")
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
	public static void start(String sourcePath, List<String> imports, boolean useTPExtension) throws IOException {
		IParser parser = (useTPExtension)? new cz.cuni.mff.d3s.trupple.language.parser.tp.Parser() : new cz.cuni.mff.d3s.trupple.language.parser.wirth.Parser();

		if (useTPExtension) {
			for (String dir : imports) {
				try {
					Files.walk(Paths.get(dir)).forEach(filePath -> {
						if (filePath.toString().endsWith(".pas")) {
							try {
								parser.Parse(Source.fromFileName(filePath.toString()));
								if (parser.hadErrors()) {
									System.err.println("Errors while parsing import file " + filePath + ".");
									return;
								}
							} catch (IOException e) {

							}
						}
					});
				} catch (NoSuchFileException e) {
					System.err.println("No such file or directory " + e.getFile());
				}
			}
		}
		
		parser.Parse(Source.fromFileName(sourcePath));
		if (parser.hadErrors()) {
			System.err.println("Errors while parsing source file, the code cannot be interpreted...");
			return;
		}

		Truffle.getRuntime().createCallTarget(parser.getRootNode()).call();
	}

	/*
	 * *************************************************************************
	 * START FROM CODES
	 */
	public static void startFromCodes(String sourceCode, List<String> imports, boolean useTPExtension) {
        IParser parser = (useTPExtension)? new cz.cuni.mff.d3s.trupple.language.parser.tp.Parser() : new cz.cuni.mff.d3s.trupple.language.parser.wirth.Parser();

        if (useTPExtension) {
            int i = 0;
            for (String imp : imports) {
                parser.Parse(Source.fromText(imp, "import" + (i++)));
                if (parser.hadErrors()) {
                    System.err.println("Errors while parsing import file " + imp + ".");
                    return;
                }
            }
        }

		parser.Parse(Source.fromText(sourceCode, "unnamed_code"));
		if (parser.hadErrors()) {
			System.err.println("Errors while parsing source file, the code cannot be interpreted...");
			return;
		}

		Truffle.getRuntime().createCallTarget(parser.getRootNode()).call();
	}

	public Node createFindContextNode1() {
		return createFindContextNode();
	}

	public PascalContext findContext1(Node contextNode) {
		return findContext(contextNode);
	}
}
