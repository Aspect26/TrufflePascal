package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Type descriptor for Pascal's built-in functions that are overloaded.
 */
public class OverloadedFunctionDescriptor extends BuiltinFunctionDescriptor {

    /**
     * List of overloads represented by subroutine descriptors.
     */
    private List<SubroutineDescriptor> overloads;

    /**
     * The default constructor. It creates an empty descriptor with no overloads.
     */
    OverloadedFunctionDescriptor() {
        super();
        this.overloads = new ArrayList<>();
    }

    /**
     * Adds new overload to the list of overloads.
     * @param subroutineDescriptor representation of the overload
     */
    void addOverLoad(SubroutineDescriptor subroutineDescriptor) {
        this.overloads.add(subroutineDescriptor);
    }


    /**
     * Gets overload matching the specified list of parameter expressions from the registered overload in this descriptor.
     */
    @Override
    public SubroutineDescriptor getOverload(List<ExpressionNode> actualParameters) throws LexicalException {
        for (SubroutineDescriptor descriptor : this.overloads) {
            if (this.compareActualWithFormatParameters(descriptor.getFormalParameters(), actualParameters)) {
                return descriptor;
            }
        }

        throw new LexicalException("Not existing overload for this subroutine");
    }

    /**
     * There are only a few built-in functions in Pascal that are overloaded and they typically do not take arguments
     * passed as reference.
     */
    @Override
    public boolean isReferenceParameter(int index) {
        return false;
    }

    /**
     * There are only a few built-in functions in Pascal that are overloaded and they typically do not take
     * subroutine-type arguments.
     */
    @Override
    public boolean isSubroutineParameter(int index) {
        return false;
    }

    @Override
    public Object getDefaultValue() {
        return this.overloads.get(0).getDefaultValue();
    }

    private boolean compareActualWithFormatParameters(List<FormalParameter> formalParameters, List<ExpressionNode> actualParameters) {
        if (formalParameters.size() != actualParameters.size()) {
            return false;
        }
        for (int i = 0; i < actualParameters.size(); ++i) {
            if (!formalParameters.get(i).type.equals(actualParameters.get(i).getType()) &&
                    !actualParameters.get(i).getType().convertibleTo(formalParameters.get(i).type)) {
                return false;
            }
        }

        return true;
    }

}
