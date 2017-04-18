package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.BuiltinNotSupportedException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.FunctionDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.ProcedureDescriptor;

import java.util.Collections;
import java.util.List;

public abstract class BuiltinFunctionDescriptor extends FunctionDescriptor {

    BuiltinFunctionDescriptor(ExpressionNode bodyNode, List<FormalParameter> parameters, TypeDescriptor returnTypeDescriptor) {
        super(parameters, returnTypeDescriptor);
        this.setRootNode(new PascalRootNode(new FrameDescriptor(), bodyNode));
    }

    @Override
    public boolean isSubroutineParameter(int index) {
        return false;
    }


    public static class NoReferenceParameterBuiltin extends BuiltinFunctionDescriptor {

        NoReferenceParameterBuiltin(ExpressionNode bodyNode, List<FormalParameter> parameters, TypeDescriptor returnTypeDescriptor) {
            super(bodyNode, parameters, returnTypeDescriptor);
        }

        @Override
        public boolean isReferenceParameter(int parameterIndex) {
            return false;
        }

    }

    public static class FullReferenceParameterBuiltin extends BuiltinFunctionDescriptor {

        public FullReferenceParameterBuiltin(ExpressionNode bodyNode, List<FormalParameter> parameters, TypeDescriptor returnTypeDescriptor) {
            super(bodyNode, parameters, returnTypeDescriptor);
        }

        @Override
        public boolean isReferenceParameter(int index) {
            return true;
        }

    }

    public static class OneArgumentBuiltin extends BuiltinFunctionDescriptor {

        public OneArgumentBuiltin(ExpressionNode bodyNode, FormalParameter parameter, TypeDescriptor returnTypeDescriptor) {
            super(bodyNode, Collections.singletonList(parameter), returnTypeDescriptor);
        }

        @Override
        public boolean hasParameters() {
            return true;
        }

    }

    public static class NotSupportedSubroutine extends NoReferenceParameterBuiltin {

        NotSupportedSubroutine(TypeDescriptor returnTypeDescriptor) {
            super(null, Collections.emptyList(), returnTypeDescriptor);
        }

        @Override
        public void verifyArguments(List<ExpressionNode> passedArguments) throws LexicalException {
            throw new BuiltinNotSupportedException();
        }

    }

}
