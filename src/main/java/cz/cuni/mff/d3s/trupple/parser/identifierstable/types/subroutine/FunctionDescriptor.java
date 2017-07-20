package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine;

import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

import java.util.List;

public class FunctionDescriptor extends SubroutineDescriptor {

    private final TypeDescriptor returnTypeDescriptor;

    public FunctionDescriptor(List<FormalParameter> formalParameters, TypeDescriptor returnTypeDescriptor) {
        super(formalParameters);
        this.returnTypeDescriptor = returnTypeDescriptor;
    }

    public TypeDescriptor getReturnDescriptor() {
        return this.returnTypeDescriptor;
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return (type instanceof FunctionDescriptor) && super.convertibleTo(type);
    }

}
