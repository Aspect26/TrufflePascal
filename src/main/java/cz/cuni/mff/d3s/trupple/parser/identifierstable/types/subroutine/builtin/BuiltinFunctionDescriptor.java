package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.FunctionPascalRootNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.ArgumentTypeMismatchException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.IncorectNumberOfArgumentsProvidedException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.EnumLiteralDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.EnumTypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.IntDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.FunctionDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BuiltinFunctionDescriptor extends FunctionDescriptor {

    BuiltinFunctionDescriptor(ExpressionNode bodyNode, List<FormalParameter> parameters) {
        super(parameters, bodyNode.getType());
        this.setRootNode(new FunctionPascalRootNode(new FrameDescriptor(), bodyNode));
    }

    BuiltinFunctionDescriptor() {
        super(new ArrayList<>(), null);
    }

    @Override
    public boolean isSubroutineParameter(int index) {
        return false;
    }

    public static class OneArgumentBuiltin extends BuiltinFunctionDescriptor {

        public OneArgumentBuiltin(ExpressionNode bodyNode, FormalParameter parameter) {
            super(bodyNode, Collections.singletonList(parameter));
        }

        @Override
        public boolean hasParameters() {
            return true;
        }

    }

    public static class OrdinalArgumentBuiltin extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

        OrdinalArgumentBuiltin(ExpressionNode bodyNode, FormalParameter parameter) {
            super(bodyNode, parameter);
        }

        @Override
        public void verifyArguments(List<ExpressionNode> arguments) throws LexicalException {
            if (arguments.size() != 1) {
                throw new IncorectNumberOfArgumentsProvidedException(1, arguments.size());
            } else {
                TypeDescriptor argumentType = arguments.get(0).getType();
                if (!argumentType.equals(IntDescriptor.getInstance()) && !argumentType.equals(LongDescriptor.getInstance()) &&
                        !argumentType.equals(CharDescriptor.getInstance()) && !argumentType.equals(BooleanDescriptor.getInstance()) &&
                        !(argumentType instanceof EnumLiteralDescriptor) && !(argumentType instanceof EnumTypeDescriptor)) {
                    throw new ArgumentTypeMismatchException(1);
                }
            }
        }
    }

    public static class NoArgumentBuiltin extends BuiltinFunctionDescriptor {

        public NoArgumentBuiltin(ExpressionNode bodyNode) {
            super(bodyNode, Collections.emptyList());
        }

    }

}
