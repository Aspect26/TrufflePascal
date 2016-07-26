package cz.cuni.mff.d3s.trupple.language.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ReadlnBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.WriteBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.WritelnBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;


/**
 * Manages the mapping from function names to their representing nodes.
 */
public class PascalFunctionRegistry {
	private final Map<String, PascalFunction> functions = new HashMap<>();
	private final PascalContext context;
	
	public PascalFunctionRegistry(PascalContext context){
		this.context = context;
		installBuiltins();
	}

	/**
     * Returns new node representing the function with name given.
     */
	public PascalFunction lookup(String name){
		PascalFunction result = functions.get(name);
		return result;
	}
	
	public void registerFunctionName(String name){
		functions.put(name, new PascalFunction("__UnimplementedFunction"));
	}
	
	public void register(String name, PascalRootNode rootNode){
		PascalFunction func = new PascalFunction(name);
		functions.put(name, func);
		RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
		func.setCallTarget(callTarget);
	}
	
	public void addAll(PascalFunctionRegistry registry){
		Iterator<Entry<String, PascalFunction>> it = registry.functions.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, PascalFunction> pair = it.next();
	        
	        String name = pair.getKey();
	        if(this.functions.containsKey(name))
	        	continue;
	        
	        this.functions.put(name, pair.getValue());
	    }
	}
	
	/**
     * Adds all builtin functions to the {@link PascalFunctionRegistry}. This method lists all
     * {@link PascalBuiltinNode builtin implementation classes}.
     */
    private void installBuiltins() {
    	
    	installBuiltinInfiniteArguments(WritelnBuiltinNodeFactory.getInstance());
    	installBuiltinInfiniteArguments(WriteBuiltinNodeFactory.getInstance());
    	//installBuiltinInfiniteArguments(ReadlnBuiltinNodeFactory.getInstance());
    	
    	//installBuiltin(IncBuiltinNodeFactory.getInstance(), registerRootNodes);
    	//installBuiltin(DecBuiltinNodeFactory.getInstance(), registerRootNodes);
    }
    
    private void installBuiltin(NodeFactory<? extends BuiltinNode> factory) {
        int argumentCount = factory.getExecutionSignature().size();
        ExpressionNode[] argumentNodes = new ExpressionNode[argumentCount];

        for (int i = 0; i < argumentCount; i++) {
            argumentNodes[i] = new ReadArgumentNode(i);
        }
        
        /* Instantiate the builtin node. This node performs the actual functionality. */
        BuiltinNode builtinBodyNode = factory.createNode(argumentNodes, context);
        
        /* Wrap the builtin in a RootNode. Truffle requires all AST to start with a RootNode. */
        PascalRootNode rootNode = new PascalRootNode(new FrameDescriptor(), builtinBodyNode);

        String name = lookupNodeInfo(builtinBodyNode.getClass()).shortName();
        this.register(name, rootNode);
    }
    
    public void installBuiltinInfiniteArguments(NodeFactory<? extends BuiltinNode> factory) {
    	ExpressionNode argumentsNode[] = new ExpressionNode[1];
    	argumentsNode[0] = new ReadAllArgumentsNode();
    	BuiltinNode bodyNode = factory.createNode(argumentsNode, context);
    	PascalRootNode rootNode = new PascalRootNode(new FrameDescriptor(), bodyNode);
    	
    	String name = lookupNodeInfo(bodyNode.getClass()).shortName();
        this.register(name, rootNode);
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
