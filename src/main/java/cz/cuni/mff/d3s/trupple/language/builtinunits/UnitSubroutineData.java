package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.parser.UnitLexicalScope;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;

/**
 * A helper structure that contains data about a subroutine. It is used in importing contents of built-in units to unit
 * lexical scope {@link BuiltinUnit#importTo(UnitLexicalScope)}.
 */
public class UnitSubroutineData {

    final SubroutineDescriptor descriptor;
    final String identifier;

    public UnitSubroutineData(String identifier, SubroutineDescriptor descriptor) {
        this.identifier = identifier;
        this.descriptor = descriptor;
    }

}
