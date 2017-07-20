package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp.RandomBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

public class RandomSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public RandomSubroutineDescriptor() {
        super(RandomBuiltinNodeGen.create(new ReadArgumentNode(0, LongDescriptor.getInstance())),
             new FormalParameter("i", LongDescriptor.getInstance(), false));
    }

}
