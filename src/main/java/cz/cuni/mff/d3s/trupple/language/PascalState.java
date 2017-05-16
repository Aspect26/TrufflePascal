package cz.cuni.mff.d3s.trupple.language;
import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.root.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

public class PascalState extends ExecutionContext {

    private final PrintWriter output;
    private final Scanner input;
    private Map<String, Object> globals;
    private Map<String, Map<String, PascalSubroutine>> unitSubroutines;
    private Map<String, VirtualFrame> unitFrames;
    private Random random;

    PascalState(PrintWriter output, InputStream input) {
        this.output = output;
        this.input = new Scanner(input);
        this.globals = new HashMap<>();
        this.random = new Random(26270);
        this.unitSubroutines = new HashMap<>();
        this.unitFrames = new HashMap<>();
    }

    public void reset() {
        this.globals = new HashMap<>();
        this.random = new Random(26270);
        this.unitSubroutines = new HashMap<>();
        this.unitFrames = new HashMap<>();
    }

    Object getExportedSymbol(String identifier) {
        return globals.get(identifier);
    }

    public Scanner getInput() {
        return this.input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public void randomize() {
        random = new Random();
    }

    public int getRandom(int upperBound) {
        return Math.abs(random.nextInt()) % upperBound;
    }

    public PascalSubroutine getSubroutine(String unitIdentifier, String subroutineIdentifier) {
        return this.unitSubroutines.get(unitIdentifier).get(subroutineIdentifier);
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

    public boolean isUnitRegistered(String unitIdentifier) {
        return this.unitFrames.containsKey(unitIdentifier);
    }

}
