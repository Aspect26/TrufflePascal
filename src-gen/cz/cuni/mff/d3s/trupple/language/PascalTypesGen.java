// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language;

import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.runtime.Null;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunction;

@GeneratedBy(PascalTypes.class)
public final class PascalTypesGen extends PascalTypes {

    @Deprecated public static final PascalTypesGen PASCALTYPES = new PascalTypesGen();

    protected PascalTypesGen() {
    }

    public static boolean isLong(Object value) {
        return value instanceof Long;
    }

    public static long asLong(Object value) {
        assert value instanceof Long : "PascalTypesGen.asLong: long expected";
        return (long) value;
    }

    public static long expectLong(Object value) throws UnexpectedResultException {
        if (value instanceof Long) {
            return (long) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isBoolean(Object value) {
        return value instanceof Boolean;
    }

    public static boolean asBoolean(Object value) {
        assert value instanceof Boolean : "PascalTypesGen.asBoolean: boolean expected";
        return (boolean) value;
    }

    public static boolean expectBoolean(Object value) throws UnexpectedResultException {
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isCharacter(Object value) {
        return value instanceof Character;
    }

    public static char asCharacter(Object value) {
        assert value instanceof Character : "PascalTypesGen.asCharacter: char expected";
        return (char) value;
    }

    public static char expectCharacter(Object value) throws UnexpectedResultException {
        if (value instanceof Character) {
            return (char) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isDouble(Object value) {
        return value instanceof Double;
    }

    public static double asDouble(Object value) {
        assert value instanceof Double : "PascalTypesGen.asDouble: double expected";
        return (double) value;
    }

    public static double expectDouble(Object value) throws UnexpectedResultException {
        if (value instanceof Double) {
            return (double) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isPascalFunction(Object value) {
        return value instanceof PascalFunction;
    }

    public static PascalFunction asPascalFunction(Object value) {
        assert value instanceof PascalFunction : "PascalTypesGen.asPascalFunction: PascalFunction expected";
        return (PascalFunction) value;
    }

    public static PascalFunction expectPascalFunction(Object value) throws UnexpectedResultException {
        if (value instanceof PascalFunction) {
            return (PascalFunction) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isEnumValue(Object value) {
        return value instanceof EnumValue;
    }

    public static EnumValue asEnumValue(Object value) {
        assert value instanceof EnumValue : "PascalTypesGen.asEnumValue: EnumValue expected";
        return (EnumValue) value;
    }

    public static EnumValue expectEnumValue(Object value) throws UnexpectedResultException {
        if (value instanceof EnumValue) {
            return (EnumValue) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isPascalArray(Object value) {
        return value instanceof PascalArray;
    }

    public static PascalArray asPascalArray(Object value) {
        assert value instanceof PascalArray : "PascalTypesGen.asPascalArray: PascalArray expected";
        return (PascalArray) value;
    }

    public static PascalArray expectPascalArray(Object value) throws UnexpectedResultException {
        if (value instanceof PascalArray) {
            return (PascalArray) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isReference(Object value) {
        return value instanceof Reference;
    }

    public static Reference asReference(Object value) {
        assert value instanceof Reference : "PascalTypesGen.asReference: Reference expected";
        return (Reference) value;
    }

    public static Reference expectReference(Object value) throws UnexpectedResultException {
        if (value instanceof Reference) {
            return (Reference) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static Null expectNull(Object value) throws UnexpectedResultException {
        if (PascalTypes.isNull(value)) {
            return PascalTypes.asPascalNull(value);
        }
        throw new UnexpectedResultException(value);
    }

}
