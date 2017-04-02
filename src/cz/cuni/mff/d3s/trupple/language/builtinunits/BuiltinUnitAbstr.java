package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.LexicalScope;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;
import java.util.List;
import java.util.Map;

public abstract class BuiltinUnitAbstr implements BuiltinUnit {

    protected abstract List<UnitFunctionData> getSubroutines();

    protected abstract Map<String, TypeDescriptor> getTypes();

    private String unitName;

    public BuiltinUnitAbstr(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public void importTo(LexicalScope lexicalScope) {
        this.importSubroutines(lexicalScope);
        this.importTypes(lexicalScope);
    }

    private void importSubroutines(LexicalScope lexicalScope) {
        List<UnitFunctionData> unitFunctions = this.getSubroutines();
        for (UnitFunctionData unitFunction : unitFunctions) {
            String identifier = unitFunction.identifier.toLowerCase();
            SubroutineDescriptor descriptor = unitFunction.descriptor;
            ExpressionNode bodyNode = unitFunction.bodyNode;

            // register to identifiers table and function registry
            lexicalScope.registerSubroutine(this.getIdentifier(identifier), bodyNode, descriptor);
        }
    }

    private void importTypes(LexicalScope lexicalScope) {
        for (Map.Entry<String, TypeDescriptor> entry : this.getTypes().entrySet()) {
            String identifier = entry.getKey();
            TypeDescriptor typeDescriptor = entry.getValue();
            lexicalScope.registerType(this.getIdentifier(identifier), typeDescriptor);
        }
    }

    private String getIdentifier(String identifier) {
        return unitName + "." + identifier;
    }

}
