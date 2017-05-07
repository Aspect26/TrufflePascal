package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.parser.UnitLexicalScope;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;
import java.util.List;

public abstract class BuiltinUnitAbstr implements BuiltinUnit {

    protected abstract List<UnitSubroutineData> getIdentifiers();

    @Override
    public void importTo(UnitLexicalScope lexicalScope) {
        List<UnitSubroutineData> unitFunctions = this.getIdentifiers();
        for (UnitSubroutineData unitFunction : unitFunctions) {
            String identifier = unitFunction.identifier.toLowerCase();
            SubroutineDescriptor descriptor = unitFunction.descriptor;

            // register to identifiers table and function registry
            lexicalScope.registerBuiltinSubroutine(identifier, descriptor);
        }
        lexicalScope.markAllIdentifiersPublic();
    }

}
