package pascal.language.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;

import pascal.language.nodes.PascalRootNode;


/**
 * Manages the mapping from function names to their representing nodes.
 */
public class PascalFunctionRegistry {
	final Map<String, PascalFunction> functions = new HashMap<>();

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
	        this.functions.put(pair.getKey(), pair.getValue());
	    }
	}
}
