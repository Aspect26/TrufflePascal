package cz.cuni.mff.d3s.trupple.parser.utils;

import cz.cuni.mff.d3s.trupple.parser.Token;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.ProcedureDescriptor;

import java.util.List;

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
