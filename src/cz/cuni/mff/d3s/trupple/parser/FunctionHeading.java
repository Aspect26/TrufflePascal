package cz.cuni.mff.d3s.trupple.parser;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.FunctionDescriptor;

import java.util.List;

public class FunctionHeading {

    public final Token identifierToken;
    public final List<FormalParameter> formalParameters;
    public final TypeDescriptor returnTypeDescriptor;
    public final FunctionDescriptor descriptor;

    public FunctionHeading(Token identifierToken, List<FormalParameter> formalParameters, TypeDescriptor returnTypeDescriptor) {
        this.identifierToken = identifierToken;
        this.formalParameters = formalParameters;
        this.returnTypeDescriptor = returnTypeDescriptor;
        this.descriptor = new FunctionDescriptor(this.formalParameters, returnTypeDescriptor);
    }
}
