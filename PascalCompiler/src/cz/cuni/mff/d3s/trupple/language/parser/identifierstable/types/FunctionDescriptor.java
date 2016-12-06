package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.FormalParameter;

import java.util.List;

public class FunctionDescriptor extends TypeDescriptor {

    private final List<FormalParameter> formalParameters;
    private final TypeDescriptor returnType;

    public FunctionDescriptor(List<FormalParameter> formalParameters, TypeDescriptor returnType) {
        this.formalParameters = formalParameters;
        this.returnType = returnType;
    }

    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }
}
