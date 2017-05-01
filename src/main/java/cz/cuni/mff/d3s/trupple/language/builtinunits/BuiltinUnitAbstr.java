package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.parser.LexicalScope;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;
import java.util.List;
import java.util.Map;

public abstract class BuiltinUnitAbstr implements BuiltinUnit {

    protected abstract List<UnitSubroutineData> getIdentifiers();

    @Override
    public void importTo(LexicalScope lexicalScope) {
        List<UnitSubroutineData> unitFunctions = this.getIdentifiers();
        for (UnitSubroutineData unitFunction : unitFunctions) {
            String identifier = unitFunction.identifier.toLowerCase();
            SubroutineDescriptor descriptor = unitFunction.descriptor;

            // register to identifiers table and function registry
            lexicalScope.registerBuiltinSubroutine(identifier, descriptor);
        }
    }

}
