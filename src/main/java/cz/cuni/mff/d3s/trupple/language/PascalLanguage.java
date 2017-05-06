package cz.cuni.mff.d3s.trupple.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.source.Source;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.parser.IParser;

import java.io.*;

/**
 * Representation of our Pascal guest language for Truffle VM. Thanks to the TruffleLanguage.Registration
 * annotation we register this class so that Truffle's PolyglotEngine will use our language.
 */
@TruffleLanguage.Registration(name = "Pascal", version = "1.0", mimeType = PascalLanguage.MIME_TYPE)
public class PascalLanguage extends TruffleLanguage<PascalState> {

    /**
     * NOTE: required by Truffle, if it is missing it won't compile
     */
    public static final PascalLanguage INSTANCE = new PascalLanguage();

    public static final String MIME_TYPE = "application/x-pascal";
    private IParser currentParser;
    private final IParser wirthParser;
    private final IParser turboParser;

    private PascalLanguage() {
        this.wirthParser = new cz.cuni.mff.d3s.trupple.parser.wirth.Parser();
        this.turboParser = new cz.cuni.mff.d3s.trupple.parser.tp.Parser();
    }

    public void setUp(boolean tpExtension, boolean extendedGoto) {
        this.currentParser = (tpExtension)? this.turboParser : this.wirthParser;
        this.currentParser.setExtendedGoto(extendedGoto);
    }

    public void reset(boolean tpExtension, boolean extendedGoto) {
        try {
            this.findContext().reset();
        } catch (NullPointerException e) {}
        this.setUp(tpExtension, extendedGoto);
    }

    @Override
    protected PascalState createContext(Env environment) {
        return new PascalState(new PrintWriter(environment.out()), environment.in());
    }

    @Override
    protected Object findExportedSymbol(PascalState state, String globalName, boolean onlyExplicit) {
        return state.getExportedSymbol(globalName);
    }

    @Override
    protected Object getLanguageGlobal(PascalState pascalState) {
        return pascalState;
    }

    @Override
    protected boolean isObjectOfLanguage(Object obj) {
        return obj instanceof PascalSubroutine;
    }

    public PascalState findContext() {
        CompilerAsserts.neverPartOfCompilation();
        return super.findContext(super.createFindContextNode());
    }

    /**
     * Gets source from the request, parses it and return call target that, if called, executes
     * given script in Pascal language.
     * @param request parsing request
     * @throws Exception the source cannot be passed
     */
    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        Source source = request.getSource();

        this.currentParser.reset();
        parseSource(source);
        return Truffle.getRuntime().createCallTarget(this.currentParser.getRootNode());
    }

    private void parseSource(Source source) throws Exception {
        this.currentParser.Parse(source);
        if (this.currentParser.hadErrors()) {
            throw new Exception("Errors while parsing source " + source.getName() + ".");
        }
    }

}
