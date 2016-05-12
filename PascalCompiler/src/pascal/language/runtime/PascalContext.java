package pascal.language.runtime;

import java.io.BufferedReader;
import java.io.PrintWriter;

import com.oracle.truffle.api.ExecutionContext;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import pascal.language.nodes.ExpressionNode;
import pascal.language.nodes.PascalRootNode;
import pascal.language.nodes.ReadArgumentNode;
import pascal.language.nodes.builtin.BuiltinNode;
import pascal.language.parser.Parser;

public final class PascalContext extends ExecutionContext {
	private final BufferedReader input;
    private final PrintWriter output;
    private final TruffleLanguage.Env env;
    private final PascalFunctionRegistry functionRegistry;
    
    public PascalContext(){
    	this(null, null, null, false);
    }
    
	public PascalContext(TruffleLanguage.Env env, BufferedReader input, PrintWriter output) {
        this(env, input, output, true);
    }

	private PascalContext(TruffleLanguage.Env env, BufferedReader input, PrintWriter output, boolean installBuiltins) {
        this.input = input;
        this.output = output;
        this.env = env;
        this.functionRegistry = new PascalFunctionRegistry();
        installBuiltins(installBuiltins);
    }
	
	public void evalSource(Source source){
		Parser.parsePascal(this, source);
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
	public PrintWriter getOutput() {
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
        installBuiltin(WritelnBuiltinNodeFactory.getInstance(), registerRootNodes);
    }
    
    public void installBuiltin(NodeFactory<? extends BuiltinNode> factory, boolean registerRootNodes) {
        /*
         * The builtin node factory is a class that is automatically generated by the Truffle DSL.
         * The signature returned by the factory reflects the signature of the @Specialization
         * methods in the builtin classes.
         */
        int argumentCount = factory.getExecutionSignature().size();
        ExpressionNode[] argumentNodes = new ExpressionNode[argumentCount];
        /*
         * Builtin functions are like normal functions, i.e., the arguments are passed in as an
         * Object[] array encapsulated in SLArguments. A SLReadArgumentNode extracts a parameter
         * from this array.
         */
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
        } else {
            // make sure the function is known
            getFunctionRegistry().lookup(name);
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
}
