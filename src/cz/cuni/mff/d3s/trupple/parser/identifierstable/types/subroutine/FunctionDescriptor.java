package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine;

import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

import java.util.List;

public class FunctionDescriptor extends SubroutineDescriptor {

    public FunctionDescriptor(List<FormalParameter> formalParameters) {
        super(formalParameters);
    }

}
