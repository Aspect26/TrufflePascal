package cz.cuni.mff.d3s.trupple.language.runtime;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.TruffleLanguage;

import cz.cuni.mff.d3s.trupple.language.parser.types.IOrdinalType;

public final class PascalContext extends ExecutionContext {
	
	// GENERIC 
	private final BufferedReader input;
	private final PrintStream output;
	private final PascalContext outerContext;
	
	public PascalContext(PascalContext outerContext) {
		this(outerContext, null, null, System.out);
	}

	private PascalContext(PascalContext outerContext, TruffleLanguage.Env env, BufferedReader input, PrintStream output) {
		this.input = input;
		this.output = output;
		this.outerContext = outerContext;
		
		this.globalFunctionRegistry = new PascalFunctionRegistry(this, true);
		this.privateFunctionRegistry = new PascalFunctionRegistry(this, false);
	}
	
	
	// FUNCTIONS
	private final PascalFunctionRegistry globalFunctionRegistry;
	private final PascalFunctionRegistry privateFunctionRegistry;
	
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
	
	// ARRAYS
	private class ArrayInfo {
		public final Object[] array;
		public final IOrdinalType ordinal;
		private final int offset;
		
		public ArrayInfo(IOrdinalType ordinal) {
			this.ordinal = ordinal;
			this.offset = ordinal.getFirstIndex();
			this.array = new Object[ordinal.getSize()];
		}
	}
	
	private Map<String, ArrayInfo> arrays = new HashMap<>();
	
	public void registerArray(String identifier, IOrdinalType ordinal) {
		arrays.put(identifier, new ArrayInfo(ordinal));
	}
}
