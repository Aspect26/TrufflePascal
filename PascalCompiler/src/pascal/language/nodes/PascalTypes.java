package pascal.language.nodes;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;

import pascal.language.runtime.PascalFunction;
import pascal.language.runtime.PascalObjectType;

@TypeSystem({int.class, long.class, boolean.class, PascalFunction.class, PascalObjectType.class})
public class PascalTypes {
	
	@ImplicitCast
	public static long castIntToLong(int value){
		return value;
	}
}
