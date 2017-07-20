package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.parser.UnitLexicalScope;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;
import java.util.List;

/**
 * Basic implementation of {@link BuiltinUnit} interface. It provides simple functionality for its descendants. They
 * only have to implement {@link BuiltinUnitAbstr#getIdentifiers()} method which returns list of data of subroutines the
 * unit contains. These subroutines are then imported to the lexical scope in
 * {@link BuiltinUnitAbstr#importTo(UnitLexicalScope)}.
 */
public abstract class BuiltinUnitAbstr implements BuiltinUnit {

    /**
     * Returns data of subroutines contained in this built-in subroutine
     */
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
