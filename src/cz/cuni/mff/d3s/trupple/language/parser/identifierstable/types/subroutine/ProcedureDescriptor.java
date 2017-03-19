package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.subroutine;

import cz.cuni.mff.d3s.trupple.language.parser.FormalParameter;

import java.util.List;

public class ProcedureDescriptor extends SubroutineDescriptor {

    public ProcedureDescriptor(List<FormalParameter> formalParameters) {
        super(formalParameters);
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

}
