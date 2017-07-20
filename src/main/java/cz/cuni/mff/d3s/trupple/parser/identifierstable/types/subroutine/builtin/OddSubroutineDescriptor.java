package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.OddBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

/**
 * Type descriptor for Pascal's <i>odd</i> built-in subroutine.
 */
public class OddSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public OddSubroutineDescriptor() {
        super(OddBuiltinNodeGen.create(new ReadArgumentNode(0, LongDescriptor.getInstance())),
                new FormalParameter("i", LongDescriptor.getInstance(), false));
    }

}
