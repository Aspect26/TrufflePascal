package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.ArctanBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

public class ArctanSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public ArctanSubroutineDescriptor() {
        super(ArctanBuiltinNodeGen.create(new ReadArgumentNode(0, RealDescriptor.getInstance())),
             new FormalParameter("i", LongDescriptor.getInstance(), false));
    }

}
