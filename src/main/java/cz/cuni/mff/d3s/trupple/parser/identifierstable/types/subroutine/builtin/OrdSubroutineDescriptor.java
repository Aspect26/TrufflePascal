package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal.OrdBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;

public class OrdSubroutineDescriptor extends BuiltinFunctionDescriptor.OrdinalArgumentBuiltin {

    public OrdSubroutineDescriptor() {
        super(OrdBuiltinNodeGen.create(new ReadArgumentNode(0, CharDescriptor.getInstance())),
             new FormalParameter("i", CharDescriptor.getInstance(), false));
    }

}
