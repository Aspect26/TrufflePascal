package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

public abstract class BuiltinProcedureDescriptor extends ProcedureDescriptor {

    private BuiltinProcedureDescriptor() {
        super(null);
    }

    private static abstract class NoReferenceParameter extends BuiltinProcedureDescriptor {

        public boolean isReferenceParameter(int parameterIndex) {
            return false;
        }
    }

    public static class Write extends NoReferenceParameter { }
    public static class Writeln extends NoReferenceParameter { }

    public static class Read extends NoReferenceParameter {

        @Override
        public boolean isReferenceParameter(int index) {
            return true;
        }
    }

    public static class Readln extends NoReferenceParameter {

        @Override
        public boolean isReferenceParameter(int index) {
            return true;
        }
    }
}
