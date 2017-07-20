package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic.TruncBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

/**
 * Type descriptor for Pascal's <i>trunc</i> built-in subroutine.
 */
public class TruncSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public TruncSubroutineDescriptor() {
        super(TruncBuiltinNodeGen.create(new ReadArgumentNode(0, RealDescriptor.getInstance())),
             new FormalParameter("i", RealDescriptor.getInstance(), false));
    }

}
