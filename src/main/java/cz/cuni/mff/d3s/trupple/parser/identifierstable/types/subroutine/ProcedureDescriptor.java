package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine;

import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

import java.util.List;

/**
 * Type descriptor specialized for procedures.
 */
public class ProcedureDescriptor extends SubroutineDescriptor {

    /**
     * The default descriptor.
     * @param formalParameters list of the procedure's formal parameters.
     */
    public ProcedureDescriptor(List<FormalParameter> formalParameters) {
        super(formalParameters);
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return (type instanceof ProcedureDescriptor) && super.convertibleTo(type);
    }

}
