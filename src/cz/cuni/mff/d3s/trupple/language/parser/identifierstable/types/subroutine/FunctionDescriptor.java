package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.subroutine;

import cz.cuni.mff.d3s.trupple.language.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;

import java.util.List;

public class FunctionDescriptor extends SubroutineDescriptor {

    private final TypeDescriptor returnType;

    public FunctionDescriptor(List<FormalParameter> formalParameters, TypeDescriptor returnType) {
        super(formalParameters);
        this.returnType = returnType;
    }

    @Override
    public Object getDefaultValue() {
        return returnType.getDefaultValue();
    }

}
