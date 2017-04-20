package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.OddBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

public class OddSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public OddSubroutineDescriptor() {
        super(
                OddBuiltinNodeFactory.create(new ReadArgumentNode(0)),
                new FormalParameter("i", LongDescriptor.getInstance(), false));
    }

}
