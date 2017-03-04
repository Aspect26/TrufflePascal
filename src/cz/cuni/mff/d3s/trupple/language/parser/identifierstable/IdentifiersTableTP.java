package cz.cuni.mff.d3s.trupple.language.parser.identifierstable;

import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.BuiltinProcedureDescriptor;

public class IdentifiersTableTP extends IdentifiersTable {

    @Override
    protected void addBuiltinFunctions() {
        super.addBuiltinFunctions();

        this.identifiersMap.put("writeln", new BuiltinProcedureDescriptor.Writeln());
        this.identifiersMap.put("readln", new BuiltinProcedureDescriptor.Readln());
    }
}
