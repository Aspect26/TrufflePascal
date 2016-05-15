package pascal.language.runtime;

import java.util.HashMap;
import java.util.Map;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;

import pascal.language.nodes.PascalRootNode;


/**
 * Manages the mapping from function names to their representing nodes.
 */
public class PascalFunctionRegistry {
	private final Map<String, PascalFunction> functions = new HashMap<>();

	/**
     * Returns new node representing the function with name given.
     */
	public PascalFunction lookup(String name){
		PascalFunction result = functions.get(name);
		return result;
	}
	
	public void register(String name, PascalRootNode rootNode){
		PascalFunction func = new PascalFunction();
		functions.put(name, func);
		RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
		func.setCallTarget(callTarget);
	}
}
