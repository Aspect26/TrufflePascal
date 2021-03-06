package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.LnBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

/**
 * Type descriptor for Pascal's <i>ln</i> built-in subroutine.
 */
public class LnSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public LnSubroutineDescriptor() {
        super(LnBuiltinNodeGen.create(new ReadArgumentNode(0, RealDescriptor.getInstance())),
                new FormalParameter("i", RealDescriptor.getInstance(), false));
    }

}
