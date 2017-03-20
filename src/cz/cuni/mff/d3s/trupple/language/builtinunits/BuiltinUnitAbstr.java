package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.parser.LexicalScope;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;
import java.util.List;

abstract class BuiltinUnitAbstr implements BuiltinUnit {

    protected abstract List<UnitFunctionData> getIdentifiers();

    @Override
    public void importTo(LexicalScope lexicalScope) {
        List<UnitFunctionData> unitFunctions = this.getIdentifiers();
        for (UnitFunctionData unitFunction : unitFunctions) {
            String identifier = unitFunction.identifier;
            SubroutineDescriptor descriptor = unitFunction.descriptor;
            BuiltinNode bodyNode = unitFunction.bodyNode;

            // register to identifiers table and function registry
            lexicalScope.registerSubroutine(identifier, bodyNode, descriptor);
        }
    }

}
