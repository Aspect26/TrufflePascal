package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp.RandomizeBuiltinNode;

/**
 * Type descriptor for Turbo Pascal's <i>randomize</i> built-in subroutine.
 */
public class RandomizeSubroutineDescriptor extends BuiltinProcedureDescriptor.NoArgumentBuiltin {

    public RandomizeSubroutineDescriptor() {
        super(new RandomizeBuiltinNode());
    }

}
