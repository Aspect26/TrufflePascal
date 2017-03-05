package cz.cuni.mff.d3s.trupple.language.runtime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.TruffleLanguage;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;

public final class PascalContext extends ExecutionContext {
	
	private final Scanner input;
	private final PrintStream output;
    private final PascalSubroutineRegistry functionRegistry;
	private final PascalContext outerContext;
	private Random random;
	
	public PascalContext(PascalContext outerContext, boolean usingTPExtension) {
		this(outerContext, null, new BufferedReader(new InputStreamReader(System.in)), System.out, usingTPExtension);
	}

	public PascalContext(PascalContext outerContext, TruffleLanguage.Env env, BufferedReader input, PrintStream output, boolean usingTPExtension) {
		this.input = new Scanner(input);
		this.output = output;
		this.outerContext = outerContext;

		this.random = new Random(165132464);
		this.functionRegistry = (usingTPExtension)? new PascalSubroutineRegistryTP(this,true) :
				new PascalSubroutineRegistry(this, true);
	}
	
	
    public void registerSubroutineName(String identifier) {
		this.functionRegistry.registerSubroutineName(identifier);
    }

	public void setSubroutineRootNode(String identifier, PascalRootNode rootNode) {
		this.functionRegistry.setFunctionRootNode(identifier, rootNode);
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

	public Scanner getInput() {
		return input;
	}

	public PrintStream getOutput() {
		return output;
	}

	public PascalSubroutineRegistry getFunctionRegistry() {
		return functionRegistry;
	}

	public boolean isImplemented(String identifier) {
        PascalFunction global = functionRegistry.lookup(identifier);
		return global.isImplemented();
	}
}
