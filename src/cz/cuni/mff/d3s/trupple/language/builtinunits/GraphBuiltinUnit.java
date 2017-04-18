package cz.cuni.mff.d3s.trupple.language.builtinunits;

import java.util.ArrayList;
import java.util.List;

public class GraphBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitSubroutineData> data = new ArrayList<>();

    @Override
    protected List<UnitSubroutineData> getIdentifiers() {
        return this.data;
    }

}
