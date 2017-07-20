package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.FunctionPascalRootNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.ArgumentTypeMismatchException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.IncorrectNumberOfArgumentsProvidedException;
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

/**
 * Base type descriptor for type descriptors for Pascal's built-in functions. It additionally to {@link FunctionDescriptor}
 * sets the function's root node in its constructor.
 */
public abstract class BuiltinFunctionDescriptor extends FunctionDescriptor {

    /**
     * The default constructor.
     * @param bodyNode body node of the function
     * @param parameters list of formal parameters of the function
     */
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

    /**
     * Specialized built-in function type descriptor for functions with single argument.
     */
    public static class OneArgumentBuiltin extends BuiltinFunctionDescriptor {

        /**
         * The default constructor.
         * @param bodyNode the body node of the function
         * @param parameter the single function's formal parameter
         */
        public OneArgumentBuiltin(ExpressionNode bodyNode, FormalParameter parameter) {
            super(bodyNode, Collections.singletonList(parameter));
        }

        @Override
        public boolean hasParameters() {
            return true;
        }

    }

    /**
     * Specialized built-in function type descriptor for functions with single ordinal-type argument.
     */
    public static class OrdinalArgumentBuiltin extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

        /**
         * The default constructor.
         * @param bodyNode the body node of the function
         * @param parameter the single ordinal-type formal parameter
         */
        OrdinalArgumentBuiltin(ExpressionNode bodyNode, FormalParameter parameter) {
            super(bodyNode, parameter);
        }

        @Override
        public void verifyArguments(List<ExpressionNode> arguments) throws LexicalException {
            if (arguments.size() != 1) {
                throw new IncorrectNumberOfArgumentsProvidedException(1, arguments.size());
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

    /**
     * Specialized built-in function type descriptor for functions with zero arguments.
     */
    public static class NoArgumentBuiltin extends BuiltinFunctionDescriptor {

        /**
         * The default constructor.
         * @param bodyNode the body node of the function
         */
        public NoArgumentBuiltin(ExpressionNode bodyNode) {
            super(bodyNode, Collections.emptyList());
        }

    }

}
