package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.allocation.NewBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;

public class NewSubroutineDescriptor extends BuiltinProcedureDescriptor.OneArgumentBuiltin {

    public NewSubroutineDescriptor() {
        super(NewBuiltinNodeFactory.create(new ReadArgumentNode(0)),
                new FormalParameter("p", new PointerDescriptor(""), false));
    }

}