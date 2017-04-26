package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine;

import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

import java.util.List;

public class ProcedureDescriptor extends SubroutineDescriptor {

    public ProcedureDescriptor(List<FormalParameter> formalParameters) {
        super(formalParameters);
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return (type instanceof ProcedureDescriptor) && super.convertibleTo(type);
    }

}
