package cz.cuni.mff.d3s.trupple.language.builtinunits;

import java.util.ArrayList;
import java.util.List;

public class CrtBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitFunctionData> data = new ArrayList<>();

    @Override
    protected List<UnitFunctionData> getIdentifiers() {
        return this.data;
    }

}
