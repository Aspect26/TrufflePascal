package cz.cuni.mff.d3s.trupple.parser;

import java.util.List;

public class ProcedureHeading {

    public final Token identifierToken;
    public final List<FormalParameter> formalParameters;

    public ProcedureHeading(Token identifierToken, List<FormalParameter> formalParameters) {
        this.identifierToken = identifierToken;
        this.formalParameters = formalParameters;
    }
}
