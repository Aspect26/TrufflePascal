package cz.cuni.mff.d3s.trupple.parser;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.IdentifiersTable;

public class RecordLexicalScope extends LexicalScope {

    RecordLexicalScope(LexicalScope outer) {
        super(outer, "_record", false);
        // this.localIdentifiers = new IdentifiersTable();
    }

}
