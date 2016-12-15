package cz.cuni.mff.d3s.trupple.language.runtime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Random;

import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.TruffleLanguage;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;

public final class PascalContext extends ExecutionContext {
	
	private final BufferedReader input;
	private final PrintStream output;
    private final PascalSubroutineRegistry globalFunctionRegistry;
    private final PascalSubroutineRegistry privateFunctionRegistry;
	private final PascalContext outerContext;
	private Random random;
	
	public PascalContext(PascalContext outerContext) {
		this(outerContext, null, new BufferedReader(new InputStreamReader(System.in)), System.out);
	}

	private PascalContext(PascalContext outerContext, TruffleLanguage.Env env, BufferedReader input, PrintStream output) {
		this.input = input;
		this.output = output;
		this.outerContext = outerContext;
		
		this.random = new Random(165132464);
		this.globalFunctionRegistry = new PascalSubroutineRegistry(this, true);
		this.privateFunctionRegistry = new PascalSubroutineRegistry(this, false);
	}
	
	
    public void registerSubroutineName(String identifier, boolean isPublic) {
        if (isPublic) {
            this.globalFunctionRegistry.registerSubroutineName(identifier);
        } else {
            this.privateFunctionRegistry.registerSubroutineName(identifier);
        }
    }

	public void setSubroutineRootNode(String identifier, PascalRootNode rootNode) {
        if (this.globalFunctionRegistry.contains(identifier)) {
            this.globalFunctionRegistry.setFunctionRootNode(identifier, rootNode);
        } else {
            this.privateFunctionRegistry.setFunctionRootNode(identifier, rootNode);
        }
    }

	
	public long getRandom(long upperBound) {
		return Math.abs(random.nextLong()) % upperBound;
	}
	
	public long getRandom() {
		return random.nextLong();
	}
	
	public void randomize() {
		this.random = new Random();
	}
	
	public PascalContext getOuterContext(){
		return outerContext;
	}

	public BufferedReader getInput() {
		return input;
	}

	public PrintStream getOutput() {
		return output;
	}

	public PascalSubroutineRegistry getGlobalFunctionRegistry() {
		return globalFunctionRegistry;
	}
	
	public PascalSubroutineRegistry getPrivateFunctionRegistry() {
		return privateFunctionRegistry;
	}
	
	public boolean containsFunction(String identifier){
		return globalFunctionRegistry.lookup(identifier) != null ||
				privateFunctionRegistry.lookup(identifier) != null;
	}
}
