package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal.ChrBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;

public class ChrSubroutineDescriptor extends BuiltinProcedureDescriptor.OneArgumentBuiltin {

    public ChrSubroutineDescriptor() {
        super(ChrBuiltinNodeFactory.create(new ReadArgumentNode(0)),
             new FormalParameter("i", new CharDescriptor(), false));
    }

}
