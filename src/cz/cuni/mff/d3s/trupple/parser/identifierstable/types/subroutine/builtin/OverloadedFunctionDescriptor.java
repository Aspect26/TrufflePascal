package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;

import java.util.ArrayList;
import java.util.List;

// NOTE: Pascal does not allow defining overloaded subroutines, only some builtin functions are overloaded
public class OverloadedFunctionDescriptor extends BuiltinFunctionDescriptor {

    private List<SubroutineDescriptor> overloads;

    OverloadedFunctionDescriptor() {
        super();
        this.overloads = new ArrayList<>();
    }

    void addOverLoad(SubroutineDescriptor subroutineDescriptor) {
        this.overloads.add(subroutineDescriptor);
    }

    public SubroutineDescriptor getOverload(List<ExpressionNode> actualParameters) throws LexicalException {
        for (SubroutineDescriptor descriptor : this.overloads) {
            if (this.compareActualWithFormatParameters(descriptor.getFormalParameters(), actualParameters)) {
                return descriptor;
            }
        }

        // TODO: create custom exception for this
        throw new LexicalException("Not existing overload for this subroutine");
    }

    @Override
    public boolean isReferenceParameter(int index) {
        return false;
    }

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
