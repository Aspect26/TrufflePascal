package pascal.language.runtime;

import java.util.HashMap;
import java.util.Map;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;

import pascal.language.nodes.PascalRootNode;


/**
 * Manages the mapping from function names to {@link PascalFunction function objects}.
 */
public class PascalFunctionRegistry {
	private final Map<String, PascalFunction> functions = new HashMap<>();

	/**
     * Returns the canonical {@link PascalFunction} object for the given name. If it does not exist yet,
     * it is created.
     */
	public PascalFunction lookup(String name){
		PascalFunction result = functions.get(name);
		if(result == null){
			result = new PascalFunction();
			functions.put(name, result);
		}
		
		return result;
	}
	
	public void register(String name, PascalRootNode rootNode){
		PascalFunction function = lookup(name);
		RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
		function.setCallTarget(callTarget);
	}
}
