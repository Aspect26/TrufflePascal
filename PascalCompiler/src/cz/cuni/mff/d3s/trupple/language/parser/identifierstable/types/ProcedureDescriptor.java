package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.FormalParameter;

import java.util.List;

public class ProcedureDescriptor extends TypeDescriptor {

    private final List<FormalParameter> formalParameters;

    public ProcedureDescriptor(List<FormalParameter> formalParameters) {
        this.formalParameters = formalParameters;
    }

    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }
}
