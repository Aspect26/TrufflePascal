package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.LnBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

public class LnSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public LnSubroutineDescriptor() {
        super(LnBuiltinNodeFactory.create(new ReadArgumentNode(0)),
                new FormalParameter("i", new LongDescriptor(), false),
                new RealDescriptor());
    }

}
