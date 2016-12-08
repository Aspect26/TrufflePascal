package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import cz.cuni.mff.d3s.trupple.language.parser.FormalParameter;
import java.util.List;

public class FunctionDescriptor extends SubroutineDescriptor {

    private final TypeDescriptor returnType;

    public FunctionDescriptor(List<FormalParameter> formalParameters, TypeDescriptor returnType) {
        super(formalParameters);
        this.returnType = returnType;
    }

}
