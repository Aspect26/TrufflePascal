package cz.cuni.mff.d3s.trupple.language.runtime;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ReadlnBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.WritelnBuiltinNodeFactory;

public class PascalSubroutineRegistryTP extends PascalSubroutineRegistry {

    PascalSubroutineRegistryTP(PascalContext context, boolean installBuiltins) {
        super(context, installBuiltins);
    }

    @Override
    protected void installBuiltins() {
        super.installBuiltins();

        installBuiltinWithVariableArgumentsCount(WritelnBuiltinNodeFactory.getInstance());
        installBuiltinWithVariableArgumentsCount(ReadlnBuiltinNodeFactory.getInstance());
    }
}
