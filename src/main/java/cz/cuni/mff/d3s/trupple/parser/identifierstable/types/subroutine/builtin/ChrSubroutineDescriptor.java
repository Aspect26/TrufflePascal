package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal.ChrBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

/**
 * Type descriptor for Pascal's <i>chr</i> built-in subroutine.
 */
public class ChrSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public ChrSubroutineDescriptor() {
        super(ChrBuiltinNodeGen.create(new ReadArgumentNode(0, LongDescriptor.getInstance())),
             new FormalParameter("i", LongDescriptor.getInstance(), false));
    }

}
