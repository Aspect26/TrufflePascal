package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal.OrdBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;

public class OrdSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public OrdSubroutineDescriptor() {
        super(OrdBuiltinNodeFactory.create(new ReadArgumentNode(0, CharDescriptor.getInstance())),
             new FormalParameter("i", CharDescriptor.getInstance(), false));
    }

}
