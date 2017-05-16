package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.CosBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

public class CosSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public CosSubroutineDescriptor() {
        super(CosBuiltinNodeGen.create(new ReadArgumentNode(0, RealDescriptor.getInstance())),
                new FormalParameter("i", LongDescriptor.getInstance(), false));
    }

}
