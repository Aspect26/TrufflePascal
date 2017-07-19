package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.allocation.NewBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;

public class NewSubroutineDescriptor extends BuiltinProcedureDescriptor.OneArgumentBuiltin {

    public NewSubroutineDescriptor() {
        super(NewBuiltinNodeGen.create(new ReadArgumentNode(0, new PointerDescriptor())),
                new FormalParameter("p", new PointerDescriptor(), false));
    }

}