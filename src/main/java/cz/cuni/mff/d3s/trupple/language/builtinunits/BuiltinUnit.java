package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.parser.UnitLexicalScope;

/**
 * Interface for representation of supported built-in units.
 */
public interface BuiltinUnit {

    /**
     * Takes care of importing represented built-in unit inside a lexical scope.
     * @param scope lexical scope into which content of the built-in unit will be imported
     */
    void importTo(UnitLexicalScope scope);

}
