package pascal.language.runtime;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.math.BigInteger;

import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.Source;

import pascal.language.nodes.ExpressionNode;
import pascal.language.nodes.PascalRootNode;
import pascal.language.nodes.builtin.BuiltinNode;
import pascal.language.nodes.builtin.ReadlnBuiltinNodeFactory;
import pascal.language.nodes.builtin.WriteBuiltinNodeFactory;
import pascal.language.nodes.builtin.WritelnBuiltinNodeFactory;
import pascal.language.nodes.call.ReadAllArgumentsNode;
import pascal.language.nodes.call.ReadArgumentNode;
import pascal.language.parser.Parser;

public final class PascalContext extends ExecutionContext {
	private final BufferedReader input;
    private final PrintStream output;
    //private final TruffleLanguage.Env env;
    private final PascalFunctionRegistry functionRegistry;
    
    public PascalContext(){
    	this(null, null, System.out, true);
    }
    
	public PascalContext(TruffleLanguage.Env env, BufferedReader input, PrintStream output) {
        this(env, input, output, true);
    }

	private PascalContext(TruffleLanguage.Env env, BufferedReader input, PrintStream output, boolean installBuiltins) {
        this.input = input;
        this.output = output;
        //this.env = env;
        this.functionRegistry = new PascalFunctionRegistry();
        installBuiltins(installBuiltins);
    }
	
	public PascalRootNode evalSource(Source source){
		Parser parser = new Parser(this, source);
		parser.Parse();
		
		if(parser.noErrors())
			return parser.mainNode;
		
		return null;
	}
	
	/**
	 * Returns the default input
	 */
	public BufferedReader getInput() {
        return input;
    }
	
	/**
	 * Returns the default output
	 */
	public PrintStream getOutput() {
        return output;
    }
	
	/**
     * Returns the registry of all functions that are currently defined.
     */
    public PascalFunctionRegistry getFunctionRegistry() {
        return functionRegistry;
    }
    
    /**
     * Adds all builtin functions to the {@link PascalFunctionRegistry}. This method lists all
     * {@link PascalBuiltinNode builtin implementation classes}.
     */
    private void installBuiltins(boolean registerRootNodes) {
    	
    	installBuiltinInfiniteArgumens(WritelnBuiltinNodeFactory.getInstance(), registerRootNodes);
    	installBuiltinInfiniteArgumens(WriteBuiltinNodeFactory.getInstance(), registerRootNodes);
    	installBuiltinInfiniteArgumens(ReadlnBuiltinNodeFactory.getInstance(), registerRootNodes);
    }
    
    public void installBuiltin(NodeFactory<? extends BuiltinNode> factory, boolean registerRootNodes) {
        int argumentCount = factory.getExecutionSignature().size();
        ExpressionNode[] argumentNodes = new ExpressionNode[argumentCount];

        for (int i = 0; i < argumentCount; i++) {
            argumentNodes[i] = new ReadArgumentNode(i);
        }
        
        /* Instantiate the builtin node. This node performs the actual functionality. */
        BuiltinNode builtinBodyNode = factory.createNode(argumentNodes, this);
        
        /* Wrap the builtin in a RootNode. Truffle requires all AST to start with a RootNode. */
        PascalRootNode rootNode = new PascalRootNode(this, new FrameDescriptor(), builtinBodyNode);

        String name = lookupNodeInfo(builtinBodyNode.getClass()).shortName();
        if (registerRootNodes) {
            /* Register the builtin function in our function registry. */
            getFunctionRegistry().register(name, rootNode);
        }
    }
    
    public void installBuiltinInfiniteArgumens(NodeFactory<? extends BuiltinNode> factory, boolean registerRootNodes) {
    	ExpressionNode argumentsNode[] = new ExpressionNode[1];
    	argumentsNode[0] = new ReadAllArgumentsNode();
    	BuiltinNode bodyNode = factory.createNode(argumentsNode, this);
    	PascalRootNode rootNode = new PascalRootNode(this, new FrameDescriptor(), bodyNode);
    	
    	String name = lookupNodeInfo(bodyNode.getClass()).shortName();
        if (registerRootNodes) {
            getFunctionRegistry().register(name, rootNode);
        }
    }
    
    public static NodeInfo lookupNodeInfo(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        NodeInfo info = clazz.getAnnotation(NodeInfo.class);
        if (info != null) {
            return info;
        } else {
            return lookupNodeInfo(clazz.getSuperclass());
        }
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
