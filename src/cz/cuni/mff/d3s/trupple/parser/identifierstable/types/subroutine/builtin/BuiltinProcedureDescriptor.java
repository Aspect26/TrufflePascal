package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.io.WriteBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.BuiltinNotSupportedException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.ProcedureDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BuiltinProcedureDescriptor extends ProcedureDescriptor {

    BuiltinProcedureDescriptor(ExpressionNode bodyNode, List<FormalParameter> parameters) {
        super(parameters);
        this.setRootNode(new PascalRootNode(new FrameDescriptor(), bodyNode));
    }

    @Override
    public boolean isSubroutineParameter(int index) {
        return false;
    }


    public static class NoReferenceParameterBuiltin extends BuiltinProcedureDescriptor {

        public NoReferenceParameterBuiltin(ExpressionNode bodyNode, List<FormalParameter> parameters) {
            super(bodyNode, parameters);
        }

        @Override
        public boolean isReferenceParameter(int parameterIndex) {
            return false;
        }

    }

    public static class FullReferenceParameterBuiltin extends BuiltinProcedureDescriptor {

        public FullReferenceParameterBuiltin(ExpressionNode bodyNode, List<FormalParameter> parameters) {
            super(bodyNode, parameters);
        }

        @Override
        public boolean isReferenceParameter(int index) {
            return true;
        }

    }

    public static class OneArgumentBuiltin extends BuiltinProcedureDescriptor {

        public OneArgumentBuiltin(ExpressionNode bodyNode, FormalParameter parameter) {
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

        public NoArgumentBuiltin(ExpressionNode bodyNode) {
            super(bodyNode, Collections.emptyList());
        }

    }

}
