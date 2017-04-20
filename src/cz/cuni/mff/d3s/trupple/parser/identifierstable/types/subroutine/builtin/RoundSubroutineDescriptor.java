package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.RoundBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

public class RoundSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public RoundSubroutineDescriptor() {
        super(RoundBuiltinNodeFactory.create(new ReadArgumentNode(0)),
             new FormalParameter("i", LongDescriptor.getInstance(), false));
    }

}
