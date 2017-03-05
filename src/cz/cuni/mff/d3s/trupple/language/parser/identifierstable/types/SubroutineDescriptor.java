package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.FormalParameter;

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

}
