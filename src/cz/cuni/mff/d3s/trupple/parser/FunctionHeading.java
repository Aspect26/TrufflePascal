package cz.cuni.mff.d3s.trupple.parser;

import java.util.List;

public class FunctionHeading {

    public final Token identifierToken;
    public final List<FormalParameter> formalParameters;
    public final Token returnTypeToken;

    public FunctionHeading(Token identifierToken, List<FormalParameter> formalParameters, Token returnTypeToken) {
        this.identifierToken = identifierToken;
        this.formalParameters = formalParameters;
        this.returnTypeToken = returnTypeToken;
    }
}
