package cz.cuni.mff.d3s.trupple.language.parser;

import com.oracle.truffle.api.frame.FrameSlot;

import java.util.HashMap;
import java.util.Map;

class LocalIdentifiersTable {

    private class IdentifierEntry {
    }

    private Map<String, IdentifierEntry> dataMap;

    public LocalIdentifiersTable() {
        this.dataMap = new HashMap<>();
    }
}
