package cz.cuni.mff.d3s.trupple.language;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;

import cz.cuni.mff.d3s.trupple.parser.IParser;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunction;
import cz.cuni.mff.d3s.trupple.parser.tp.Parser;

@TruffleLanguage.Registration(name = "Pascal", version = "1.0", mimeType = PascalLanguage.MIME_TYPE)
public final class PascalLanguage extends TruffleLanguage<PascalContext> {

    public static final PascalLanguage INSTANCE = new PascalLanguage();
	static final String MIME_TYPE = "text/x-pascal";

	public static PascalContext createContext() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintStream output = new PrintStream(System.out, true);

        PascalContext.getInstance().setInput(new Scanner(input));
        PascalContext.getInstance().setOutput(output);

        return PascalContext.getInstance();
    }

	@Override
	protected PascalContext createContext(com.oracle.truffle.api.TruffleLanguage.Env environment) {
        return createContext();
	}

	@Override
	protected Object findExportedSymbol(PascalContext context, String globalName, boolean onlyExplicit) {
		return null;
	}

	@Override
	protected Object getLanguageGlobal(PascalContext context) {
		return context;
	}

	@Override
	protected boolean isObjectOfLanguage(Object object) {
		return object instanceof PascalFunction;
	}

	@Override
	protected Object evalInContext(Source source, Node node, MaterializedFrame mFrame) throws IOException {
		return null;
	}

    @Override
    protected CallTarget parse(Source code, Node context, String... argumentNames) throws IOException {
        // TODO: implement this
	    return null;
    }

	public static void start(String sourcePath, String[] arguments, List<String> imports, boolean useTPExtension,
                             boolean extendedGotoSupport) throws IOException {
	    createContext();
		IParser parser = (useTPExtension)? new Parser(extendedGotoSupport) :
                new cz.cuni.mff.d3s.trupple.parser.wirth.Parser(extendedGotoSupport);

		if (useTPExtension && !imports.isEmpty()) {
            if (!parseImports(imports, parser)) {
                return;
            }
		}
		
		if (!parseSource(buildSourceFromFile(new File(sourcePath)), parser)) {
		    return;
        }

		Truffle.getRuntime().createCallTarget(parser.getRootNode()).call(arguments);
	}

	public static void startFromCodes(String sourceCode, List<String> imports, boolean useTPExtension,
                                      boolean extendedGotoSupport) {
	    createContext();
        IParser parser = (useTPExtension)? new Parser(extendedGotoSupport) :
                new cz.cuni.mff.d3s.trupple.parser.wirth.Parser(extendedGotoSupport);

        if (useTPExtension) {
            int i = 0;
            for (String imp : imports) {
                if (!parseSource(buildSourceFromText(imp, "import" + (i++)), parser)) {
                    return;
                }
            }
        }

        if (!parseSource(buildSourceFromText(sourceCode, "unnamed_code"), parser)) {
            return;
        }

		Truffle.getRuntime().createCallTarget(parser.getRootNode()).call();
	}

	private static boolean parseImports(List<String> imports, IParser parser) {
        for (String dir : imports) {
            if (!parseImportDirectory(dir, parser)) {
                return false;
            }
        }

        return true;
    }

    private static boolean parseImportDirectory(String path, IParser parser) {
	    File[] filesInDirectory = new File(path).listFiles();

	    if (filesInDirectory == null) {
	        return true;
        }

	    for (File fileImport : filesInDirectory) {
	        if (fileImport.isFile() && fileImport.getAbsolutePath().endsWith(".pas")) {
	            if (!parseImport(fileImport, parser)) {
	                return false;
                }
            }
        }

        return true;
    }

    private static boolean parseImport(File importFile, IParser parser) {
        String filePath = importFile.getPath();

        try {
            parser.Parse(buildSourceFromFile(importFile));

            if (parser.hadErrors()) {
                System.err.println("Errors while parsing import file " + filePath + ".");
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error reading unit file: " + filePath);
        }

        return true;
    }

    private static boolean parseSource(Source source, IParser parser) {
	    parser.Parse(source);
        if (parser.hadErrors()) {
            System.err.println("Errors while parsing source file, the code cannot be interpreted...");
            return false;
        }

        return true;
    }

    private static Source buildSourceFromText(String source, String name) {
	    try {
            InputStream is = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
            return Source.newBuilder(new InputStreamReader(is)).name(name).mimeType(PascalLanguage.MIME_TYPE).build();
        } catch (IOException e) {
	        return null;
        }
    }

    private static Source buildSourceFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        return Source.newBuilder(new InputStreamReader(is)).name(file.getName()).mimeType(PascalLanguage.MIME_TYPE).build();
    }
}
