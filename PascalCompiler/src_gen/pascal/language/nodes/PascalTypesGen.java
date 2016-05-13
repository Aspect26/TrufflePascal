// CheckStyle: start generated
package pascal.language.nodes;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import pascal.language.runtime.PascalFunction;

@GeneratedBy(PascalTypes.class)
public final class PascalTypesGen extends PascalTypes {

    @Deprecated public static final PascalTypesGen PASCALTYPES = new PascalTypesGen();

    protected PascalTypesGen() {
    }

    public static boolean isInteger(Object value) {
        return value instanceof Integer;
    }

    public static int asInteger(Object value) {
        assert value instanceof Integer : "PascalTypesGen.asInteger: int expected";
        return (int) value;
    }

    public static int expectInteger(Object value) throws UnexpectedResultException {
        if (value instanceof Integer) {
            return (int) value;
        }
        throw new UnexpectedResultException(value);
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

    public static long asImplicitLong(Object value) {
        if (value instanceof Long) {
            return (long) value;
        } else if (value instanceof Integer) {
            return castIntToLong((int) value);
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Illegal type ");
        }
    }

    public static boolean isImplicitLong(Object value) {
        return value instanceof Long
             || value instanceof Integer;
    }

    public static long asImplicitLong(Object value, Class<?> typeHint) {
        if (typeHint == long.class) {
            return (long) value;
        } else if (typeHint == int.class) {
            return castIntToLong((int) value);
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Illegal type ");
        }
    }

    public static long expectImplicitLong(Object value, Class<?> typeHint) throws UnexpectedResultException {
        if (typeHint == long.class && value instanceof Long) {
            return (long) value;
        } else if (typeHint == int.class && value instanceof Integer) {
            return castIntToLong((int) value);
        } else {
            throw new UnexpectedResultException(value);
        }
    }

    public static boolean isImplicitLong(Object value, Class<?> typeHint) {
        return (typeHint == long.class && value instanceof Long)
             || (typeHint == int.class && value instanceof Integer);
    }

    public static Class<?> getImplicitLongClass(Object value) {
        if (value instanceof Long) {
            return long.class;
        } else if (value instanceof Integer) {
            return int.class;
        } else if (value == null) {
            return Object.class;
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Illegal type ");
        }
    }

}
