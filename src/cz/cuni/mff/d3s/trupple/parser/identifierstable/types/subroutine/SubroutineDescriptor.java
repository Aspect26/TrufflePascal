package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

import java.util.List;

public abstract class SubroutineDescriptor implements TypeDescriptor {

    protected final List<FormalParameter> formalParameters;

    SubroutineDescriptor(List<FormalParameter> formalParameters) {
        this.formalParameters = formalParameters;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    public boolean hasParameters() {
        return this.formalParameters.size() != 0;
    }

    public boolean isReferenceParameter(int parameterIndex) {
        return this.formalParameters.get(parameterIndex).isReference;
    }

    public void verifyArguments(List<ExpressionNode> passedArguments) throws LexicalException {

    }

}
