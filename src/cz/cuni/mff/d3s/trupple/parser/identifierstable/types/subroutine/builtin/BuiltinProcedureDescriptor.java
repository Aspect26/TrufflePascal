package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.ProcedurePascalRootNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.BuiltinNotSupportedException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.ProcedureDescriptor;

import java.util.Collections;
import java.util.List;

public abstract class BuiltinProcedureDescriptor extends ProcedureDescriptor {

    BuiltinProcedureDescriptor(StatementNode bodyNode, List<FormalParameter> parameters) {
        super(parameters);
        this.setRootNode(new ProcedurePascalRootNode(new FrameDescriptor(), bodyNode));
    }

    @Override
    public boolean isSubroutineParameter(int index) {
        return false;
    }


    public static class NoReferenceParameterBuiltin extends BuiltinProcedureDescriptor {

        public NoReferenceParameterBuiltin(StatementNode bodyNode, List<FormalParameter> parameters) {
            super(bodyNode, parameters);
        }

        @Override
        public boolean isReferenceParameter(int parameterIndex) {
            return false;
        }

    }

    public static class FullReferenceParameterBuiltin extends BuiltinProcedureDescriptor {

        public FullReferenceParameterBuiltin(StatementNode bodyNode, List<FormalParameter> parameters) {
            super(bodyNode, parameters);
        }

        @Override
        public boolean isReferenceParameter(int index) {
            return true;
        }

    }

    public static class OneArgumentBuiltin extends BuiltinProcedureDescriptor {

        public OneArgumentBuiltin(StatementNode bodyNode, FormalParameter parameter) {
            super(bodyNode, Collections.singletonList(parameter));
        }

        @Override
        public boolean hasParameters() {
            return true;
        }

    }

    public static class NotSupportedSubroutine extends NoReferenceParameterBuiltin {

        public NotSupportedSubroutine() {
            super(null, Collections.emptyList());
        }

        @Override
        public void verifyArguments(List<ExpressionNode> passedArguments) throws LexicalException {
            throw new BuiltinNotSupportedException();
        }

    }

    public static class NoArgumentBuiltin extends BuiltinProcedureDescriptor {

        public NoArgumentBuiltin(StatementNode bodyNode) {
            super(bodyNode, Collections.emptyList());
        }

    }

}
