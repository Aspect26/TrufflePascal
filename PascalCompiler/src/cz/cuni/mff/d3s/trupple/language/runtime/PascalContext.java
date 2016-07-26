package cz.cuni.mff.d3s.trupple.language.runtime;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.math.BigInteger;

import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ReadlnBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.WriteBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.WritelnBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;

public final class PascalContext extends ExecutionContext {
	private final BufferedReader input;
    private final PrintStream output;
    //private final TruffleLanguage.Env env;
    private final PascalFunctionRegistry functionRegistry;
    
    public PascalContext(){
    	this(null, null, System.out);
    }
    
	private PascalContext(TruffleLanguage.Env env, BufferedReader input, PrintStream output) {
        this.input = input;
        this.output = output;
        //this.env = env;
        this.functionRegistry = new PascalFunctionRegistry(this);
    }
	
	public BufferedReader getInput() {
        return input;
    }
	
	public PrintStream getOutput() {
        return output;
    }
	
    public PascalFunctionRegistry getFunctionRegistry() {
        return functionRegistry;
    }
    
    public static Object fromForeignValue(Object a) {
        if (a instanceof Long || a instanceof BigInteger || a instanceof String) {
            return a;
        } else if (a instanceof Number) {
            return ((Number) a).longValue();
        } else if (a instanceof TruffleObject) {
            return a;
        } else if (a instanceof PascalContext) {
            return a;
        }
        throw new IllegalStateException(a + " is not a Truffle value");
    }
}
