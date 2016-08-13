package cz.cuni.mff.d3s.trupple.language.runtime;

import java.io.BufferedReader;
import java.io.PrintStream;

import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.TruffleLanguage;

public final class PascalContext extends ExecutionContext {
	private final BufferedReader input;
	private final PrintStream output;
	// private final TruffleLanguage.Env env;
	
	private final PascalFunctionRegistry globalFunctionRegistry;
	private final PascalFunctionRegistry privateFunctionRegistry;
	private final PascalContext outerContext;
	
	public PascalContext(PascalContext outerContext) {
		this(outerContext, null, null, System.out);
	}

	private PascalContext(PascalContext outerContext, TruffleLanguage.Env env, BufferedReader input, PrintStream output) {
		this.input = input;
		this.output = output;
		this.outerContext = outerContext;
		// this.env = env;
		
		this.globalFunctionRegistry = new PascalFunctionRegistry(this, true);
		this.privateFunctionRegistry = new PascalFunctionRegistry(this, false);
	}
	
	public boolean containsParameterlessSubroutine(String identifier){
		PascalContext context = this;
		PascalFunction func;
		
		while(context != null){
			func = globalFunctionRegistry.lookup(identifier);
			if(func != null && func.getParametersCount() == 0)
				return true;
			
			func = privateFunctionRegistry.lookup(identifier);
			if(func != null && func.getParametersCount() == 0)
				return true;
			
			context = context.outerContext;
		}
		
		return false;
	}
	
	public void setMySubroutineParametersCount(String identifier, int count) {
		PascalFunction func = this.globalFunctionRegistry.lookup(identifier);
		if(func == null){
			func = this.privateFunctionRegistry.lookup(identifier);
		}
		
		assert func != null;
		
		func.setParametersCount(count);
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

	public PascalFunctionRegistry getGlobalFunctionRegistry() {
		return globalFunctionRegistry;
	}
	
	public PascalFunctionRegistry getPrivateFunctionRegistry() {
		return privateFunctionRegistry;
	}
	
	public boolean containsIdentifier(String identifier){
		return globalFunctionRegistry.lookup(identifier) != null ||
				privateFunctionRegistry.lookup(identifier) != null;
	}
}
