package cz.cuni.mff.d3s.trupple.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.Source;
import cz.cuni.mff.d3s.trupple.language.nodes.root.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.parser.IParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

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
    private Random random;
    private Map<String, VirtualFrame> unitFrames;
    private Map<String, Map<String, PascalSubroutine>> unitSubroutines;
    private Scanner input = new Scanner(System.in);

    private PascalLanguage() {
        this.wirthParser = new cz.cuni.mff.d3s.trupple.parser.wirth.Parser();
        this.turboParser = new cz.cuni.mff.d3s.trupple.parser.tp.Parser();
        this.reset(false, false);
    }

    public void reset(boolean tpExtension, boolean extendedGoto) {
        this.currentParser = (tpExtension)? this.turboParser : this.wirthParser;
        this.currentParser.setExtendedGoto(extendedGoto);
        random = new Random(26270);
        unitFrames = new HashMap<>();
        unitSubroutines = new HashMap<>();
        input = new Scanner(System.in);
    }

    @Override
    protected PascalState createContext(Env environment) {
        return new PascalState();
    }

    @Override
    protected Object findExportedSymbol(PascalState state, String globalName, boolean onlyExplicit) {
        return null;
    }

    @Override
    protected Object getLanguageGlobal(PascalState pascalState) {
        return pascalState;
    }

    @Override
    protected boolean isObjectOfLanguage(Object obj) {
        return obj instanceof PascalSubroutine;
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

    public void randomize() {
        random = new Random();
    }

    public int getRandom(int upperBound) {
        return Math.abs(random.nextInt()) % upperBound;
    }

    public boolean isUnitRegistered(String unitIdentifier) {
        return this.unitFrames.containsKey(unitIdentifier);
    }

    public VirtualFrame getUnitFrame(String unitIdentifier) {
        return this.unitFrames.get(unitIdentifier);
    }

    public VirtualFrame createUnitFrame(String unitIdentifier, FrameDescriptor frameDescriptor) {
        VirtualFrame unitFrame = Truffle.getRuntime().createVirtualFrame(new Object[0], frameDescriptor);
        this.unitFrames.put(unitIdentifier, unitFrame);

        return unitFrame;
    }

    public void updateSubroutine(String unitIdentifier, String subroutineIdentifier, PascalRootNode rootNode) {
        if (!this.unitSubroutines.containsKey(unitIdentifier)) {
            this.unitSubroutines.put(unitIdentifier, new HashMap<>());
        }
        this.unitSubroutines.get(unitIdentifier).put(subroutineIdentifier, new PascalSubroutine(Truffle.getRuntime().createCallTarget(rootNode)));
    }

    public PascalSubroutine getSubroutine(String unitIdentifier, String subroutineIdentifier) {
        return this.unitSubroutines.get(unitIdentifier).get(subroutineIdentifier);
    }

    public Scanner getInput() {
        return this.input;
    }

    public void setInput(InputStream is) {
        this.input = new Scanner(is);
    }

}
