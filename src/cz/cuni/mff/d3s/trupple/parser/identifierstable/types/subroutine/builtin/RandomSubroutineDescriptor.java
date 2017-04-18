package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp.RandomBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

public class RandomSubroutineDescriptor extends BuiltinProcedureDescriptor.OneArgumentBuiltin {

    public RandomSubroutineDescriptor() {
        super(RandomBuiltinNodeFactory.create(new ReadArgumentNode(0)),
             new FormalParameter("i", new LongDescriptor(), false));
    }

}
