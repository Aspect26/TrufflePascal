package pascal.language;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;

import pascal.language.runtime.PascalFunction;

@TypeSystem({int.class, long.class, boolean.class, PascalFunction.class})
public class PascalTypes {
	
	@ImplicitCast
	public static long castIntToLong(int value){
		return value;
	}
}
