package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.allocation.DisposeBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;

public class DisposeSubroutineDescriptor extends BuiltinProcedureDescriptor.OneArgumentBuiltin {

    public DisposeSubroutineDescriptor() {
        super(DisposeBuiltinNodeGen.create(new ReadArgumentNode(0)),
                new FormalParameter("p", new PointerDescriptor(""), false));
    }

}