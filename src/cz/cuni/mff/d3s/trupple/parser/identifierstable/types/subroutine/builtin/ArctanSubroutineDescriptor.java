package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.ArctanBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

public class ArctanSubroutineDescriptor extends BuiltinProcedureDescriptor.OneArgumentBuiltin {

    public ArctanSubroutineDescriptor() {
        super(ArctanBuiltinNodeFactory.create(new ReadArgumentNode(0)),
             new FormalParameter("i", LongDescriptor.getInstance(), false));
    }

}
