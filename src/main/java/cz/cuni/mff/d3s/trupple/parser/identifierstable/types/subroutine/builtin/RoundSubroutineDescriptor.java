package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.RoundBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

/**
 * Type descriptor for Pascal's <i>round</i> built-in subroutine.
 */
public class RoundSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public RoundSubroutineDescriptor() {
        super(RoundBuiltinNodeGen.create(new ReadArgumentNode(0, RealDescriptor.getInstance())),
             new FormalParameter("i", RealDescriptor.getInstance(), false));
    }

}
