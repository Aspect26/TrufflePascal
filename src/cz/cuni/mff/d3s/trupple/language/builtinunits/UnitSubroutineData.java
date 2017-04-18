package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;

public class UnitSubroutineData {

    final SubroutineDescriptor descriptor;
    final String identifier;

    public UnitSubroutineData(String identifier, SubroutineDescriptor descriptor) {
        this.identifier = identifier;
        this.descriptor = descriptor;
    }

}
