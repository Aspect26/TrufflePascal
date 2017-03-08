package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.subroutine;

import java.util.Collections;

public abstract class BuiltinProcedureDescriptor extends ProcedureDescriptor {

    private BuiltinProcedureDescriptor() {
        super(Collections.emptyList());
    }


    public static class NoReferenceParameterBuiltin extends BuiltinProcedureDescriptor {

        @Override
        public boolean isReferenceParameter(int parameterIndex) {
            return false;
        }
    }

    public static class FullReferenceParameterBuiltin extends BuiltinProcedureDescriptor {

        @Override
        public boolean isReferenceParameter(int index) {
            return true;
        }
    }

}
