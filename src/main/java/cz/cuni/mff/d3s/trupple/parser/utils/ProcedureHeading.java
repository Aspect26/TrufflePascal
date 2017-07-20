package cz.cuni.mff.d3s.trupple.parser.utils;

import cz.cuni.mff.d3s.trupple.parser.Token;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.ProcedureDescriptor;

import java.util.List;

/**
 * Structure defining a procedure signature. It contains procedure's identifier's token (from parser), list of its
 * formal parameters and the procedure's descriptor.
 */
public class ProcedureHeading {

    public final Token identifierToken;
    public final List<FormalParameter> formalParameters;
    public final ProcedureDescriptor descriptor;

    public ProcedureHeading(Token identifierToken, List<FormalParameter> formalParameters) {
        this.identifierToken = identifierToken;
        this.formalParameters = formalParameters;
        this.descriptor = new ProcedureDescriptor(formalParameters);
    }
}
