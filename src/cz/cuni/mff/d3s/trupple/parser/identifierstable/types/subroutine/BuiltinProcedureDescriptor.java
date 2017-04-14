package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.exceptions.BuiltinNotSupportedException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;

import java.util.Collections;
import java.util.List;

public abstract class BuiltinProcedureDescriptor extends ProcedureDescriptor {

    private BuiltinProcedureDescriptor() {
        super(Collections.emptyList());
    }

    @Override
    public boolean isSubroutineParameter(int index) {
        // NOTE: no builtin subroutine takes subroutine as an argument
        return false;
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

    public static class OneArgumentBuiltin extends BuiltinProcedureDescriptor {

        public boolean hasParameters() {
            return true;
        }

        @Override
        public boolean isReferenceParameter(int parameterIndex) {
            return false;
        }

    }

    public static class NotSupportedSubroutine extends NoReferenceParameterBuiltin {

        @Override
        public void verifyArguments(List<ExpressionNode> passedArguments) throws LexicalException {
            throw new BuiltinNotSupportedException();
        }
    }

}
