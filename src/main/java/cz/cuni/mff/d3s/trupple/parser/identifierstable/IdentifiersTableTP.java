package cz.cuni.mff.d3s.trupple.parser.identifierstable;

import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.StringDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.AssignSubroutineDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.ExitSubroutineDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.RandomSubroutineDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.RandomizeSubroutineDescriptor;

public class IdentifiersTableTP extends IdentifiersTable {

    @Override
    protected void addBuiltinTypes() {
        this.typeDescriptors.put("string", StringDescriptor.getInstance());

        super.addBuiltinTypes();
    }

    @Override
    protected void addBuiltinFunctions() {
        super.addBuiltinFunctions();
        try {
            this.registerNewIdentifier("random", new RandomSubroutineDescriptor());
            this.registerNewIdentifier("randomize", new RandomizeSubroutineDescriptor());
            this.registerNewIdentifier("assign", new AssignSubroutineDescriptor());
            this.registerNewIdentifier("exit", new ExitSubroutineDescriptor());
        } catch (LexicalException e) {
            throw new PascalRuntimeException("Could not initialize extension builtin functions: " + e.getMessage());
        }
    }

}
