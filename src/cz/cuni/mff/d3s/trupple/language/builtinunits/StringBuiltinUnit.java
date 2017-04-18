package cz.cuni.mff.d3s.trupple.language.builtinunits;

import java.util.ArrayList;
import java.util.List;

public class StringBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitSubroutineData> data = new ArrayList<>();

    public StringBuiltinUnit() {
        this.initialize();
    }

    private void initialize() {

    }

    @Override
    protected List<UnitSubroutineData> getIdentifiers() {
        return this.data;
    }

}
