package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.file.EolnBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.TextFileDescriptor;

/**
 * Type descriptor for Pascal's <i>eoln</i> built-in subroutine.
 */
public class EolnSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public EolnSubroutineDescriptor() {
        super(EolnBuiltinNodeGen.create(new ExpressionNode[]{new ReadAllArgumentsNode()}),
                new FormalParameter("p", TextFileDescriptor.getInstance(), false));
    }

}