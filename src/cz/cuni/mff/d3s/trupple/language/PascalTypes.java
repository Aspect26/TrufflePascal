package cz.cuni.mff.d3s.trupple.language;

import com.oracle.truffle.api.dsl.TypeCast;
import com.oracle.truffle.api.dsl.TypeCheck;
import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.dsl.internal.DSLOptions;

import cz.cuni.mff.d3s.trupple.language.customvalues.*;
import cz.cuni.mff.d3s.trupple.language.runtime.Null;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunction;

@TypeSystem({ long.class, boolean.class, char.class, double.class, PascalFunction.class, EnumValue.class, PascalArray.class,
	Reference.class, PointerValue.class, SetTypeValue.class, FileValue.class, Null.class })
@DSLOptions
public class PascalTypes {

	@TypeCheck(Null.class)
	public static boolean isNull(Object value) {
		return value == Null.SINGLETON;
	}

	@TypeCast(Null.class)
	public static Null asPascalNull(Object value) {
		assert isNull(value);
		return Null.SINGLETON;
	}
}