package cz.cuni.mff.d3s.trupple.parser;

/**
 * Specified lexical scope for records. Not really useful in its current state.
 */
class RecordLexicalScope extends LexicalScope {

    RecordLexicalScope(LexicalScope outer) {
        super(outer, "_record", false);
    }

}
