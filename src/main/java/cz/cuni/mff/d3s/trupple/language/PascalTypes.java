package cz.cuni.mff.d3s.trupple.language;

import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.TypeSystem;

import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.*;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;

@TypeSystem({ long.class, boolean.class, char.class, double.class, PascalSubroutine.class, PascalString.class,
        EnumValue.class, PascalArray.class, Reference.class, PointerValue.class, SetTypeValue.class, FileValue.class, })
public class PascalTypes {

    @ImplicitCast
    public static double castLongToDouble(long value) {
        return value;
    }

    @ImplicitCast
    public static PascalString castCharToString(char c) {
        return new PascalString(String.valueOf(c));
    }
    
}