package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.AbsBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

public class AbsSubroutineDescriptor extends BuiltinProcedureDescriptor.OneArgumentBuiltin {

    public AbsSubroutineDescriptor() {
        super(
                AbsBuiltinNodeFactory.create(new ReadArgumentNode(0)),
                new FormalParameter("i", new LongDescriptor(), false));
    }

}
