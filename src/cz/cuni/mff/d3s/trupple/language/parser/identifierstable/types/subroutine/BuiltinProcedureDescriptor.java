package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.subroutine;

public abstract class BuiltinProcedureDescriptor extends ProcedureDescriptor {

    private BuiltinProcedureDescriptor() {
        super(null);
    }


    private static abstract class NoReferenceParameterBuiltin extends BuiltinProcedureDescriptor {

        public boolean isReferenceParameter(int parameterIndex) {
            return false;
        }
    }

    // **********************************************************************
    // Actual builtin subroutine's descriptors
    // **********************************************************************

    public static class Write extends NoReferenceParameterBuiltin { }

    public static class Writeln extends NoReferenceParameterBuiltin {
    }

    public static class Read extends BuiltinProcedureDescriptor {

        @Override
        public boolean isReferenceParameter(int index) {
            return true;
        }
    }

    public static class Readln extends BuiltinProcedureDescriptor {

        @Override
        public boolean isReferenceParameter(int index) {
            return true;
        }

        }
}
