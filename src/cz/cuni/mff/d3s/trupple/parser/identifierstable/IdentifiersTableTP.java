package cz.cuni.mff.d3s.trupple.parser.identifierstable;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.StringDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.BuiltinProcedureDescriptor;

public class IdentifiersTableTP extends IdentifiersTable {

    @Override
    protected void addBuiltinTypes() {
        this.typeDescriptors.put("string", new StringDescriptor());

        super.addBuiltinTypes();
    }

    @Override
    protected void addBuiltinFunctions() {
        super.addBuiltinFunctions();

        this.identifiersMap.put("writeln", new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin());
        this.identifiersMap.put("readln", new BuiltinProcedureDescriptor.FullReferenceParameterBuiltin());
        this.identifiersMap.put("random", new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin());
        this.identifiersMap.put("randomize", new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin());
        this.identifiersMap.put("assign", new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin());
    }

}
