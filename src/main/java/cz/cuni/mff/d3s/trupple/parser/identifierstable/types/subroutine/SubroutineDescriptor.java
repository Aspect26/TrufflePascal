package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.nodes.RootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.ArgumentTypeMismatchException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.IncorrectNumberOfArgumentsProvidedException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

import java.util.List;

/**
 * Base type descriptor for subroutines. It contains the list of its formal parameters and an instance of {@link PascalSubroutine}
 * representing the subroutine.
 */
public abstract class SubroutineDescriptor implements TypeDescriptor {

    protected final List<FormalParameter> formalParameters;

    protected PascalSubroutine subroutine;

    /**
     * The default constructor.
     * @param formalParameters list of the subroutine's formal parameters
     */
    SubroutineDescriptor(List<FormalParameter> formalParameters) {
        this.formalParameters = formalParameters;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return this.subroutine;
    }

    /**
     * Gets the descriptor of overload of this subroutine matching the specified arguments. Used only in
     * {@link cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.OverloadedFunctionDescriptor}.
     */
    public SubroutineDescriptor getOverload(List<ExpressionNode> arguments) throws LexicalException {
        return this;
    }

    public List<FormalParameter> getFormalParameters() {
        return this.formalParameters;
    }

    public PascalSubroutine getSubroutine() {
        return this.subroutine;
    }

    public void setRootNode(RootNode rootNode) {
        RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
        this.subroutine = new PascalSubroutine(callTarget);
    }

    public PascalRootNode getRootNode() {
        return (PascalRootNode) this.subroutine.getCallTarget().getRootNode();
    }

    public boolean hasParameters() {
        return this.formalParameters.size() != 0;
    }

    /**
     * Checks whether this subroutine's parameter at the specified index is passed by reference.
     */
    public boolean isReferenceParameter(int parameterIndex) {
        return this.formalParameters.get(parameterIndex).isReference;
    }

    /**
     * Checks whether this subroutine's parameter at the specified index is a subroutine-type parameter.
     */
    public boolean isSubroutineParameter(int parameterIndex) {
        return this.formalParameters.get(parameterIndex).isSubroutine;
    }

    /**
     * Verifies whether the types of the specified expressions match this subroutine's formal parameters.
     */
    public void verifyArguments(List<ExpressionNode> passedArguments) throws LexicalException {
        if (passedArguments.size() != this.formalParameters.size()) {
            throw new IncorrectNumberOfArgumentsProvidedException(this.formalParameters.size(), passedArguments.size());
        } else {
            for (int i = 0; i < this.formalParameters.size(); ++i) {
                if (!this.formalParameters.get(i).type.equals(passedArguments.get(i).getType()) &&
                        !passedArguments.get(i).getType().convertibleTo(this.formalParameters.get(i).type)) {
                    throw new ArgumentTypeMismatchException(i + 1);
                }
            }
        }
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        if (!(type instanceof SubroutineDescriptor)) {
            return false;
        }

        SubroutineDescriptor subroutine = (SubroutineDescriptor) type;
        return compareFormalParameters(subroutine.formalParameters, this.formalParameters);
    }

    private static boolean compareFormalParameters(List<FormalParameter> left, List<FormalParameter> right) {
        if (left.size() != right.size()) {
            return false;
        }
        for (int i = 0; i < right.size(); ++i) {
            if (!right.get(i).type.equals(left.get(i).type) &&
                    !left.get(i).type.convertibleTo(right.get(i).type)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares the selected lists of formal parameters whether they are equal.
     */
    public static boolean compareFormalParametersExact(List<FormalParameter> left, List<FormalParameter> right) {
        if (left.size() != right.size()) {
            return false;
        }
        for (int i = 0; i < right.size(); ++i) {
            if (!right.get(i).type.equals(left.get(i).type)) {
                return false;
            }
        }

        return true;
    }

}
