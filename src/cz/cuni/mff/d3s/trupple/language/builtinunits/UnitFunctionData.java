package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;

public class UnitFunctionData {

    final BuiltinNode bodyNode;
    final SubroutineDescriptor descriptor;
    final String identifier;

    public UnitFunctionData(String identifier, SubroutineDescriptor descriptor, BuiltinNode bodyNode) {
        this.identifier = identifier;
        this.bodyNode = bodyNode;
        this.descriptor = descriptor;
    }

}
