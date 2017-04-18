package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.io.ReadBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;

import java.util.ArrayList;

public class ReadSubroutineDescriptor extends BuiltinProcedureDescriptor.FullReferenceParameterBuiltin {

    public ReadSubroutineDescriptor() {
        super(ReadBuiltinNodeFactory.create(new ExpressionNode[]{new ReadAllArgumentsNode()}), new ArrayList<>());
    }

}
