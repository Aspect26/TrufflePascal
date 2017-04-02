package cz.cuni.mff.d3s.trupple.language.builtinunits.strings;

import cz.cuni.mff.d3s.trupple.language.builtinunits.BuiltinUnitAbstr;
import cz.cuni.mff.d3s.trupple.language.builtinunits.UnitFunctionData;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitFunctionData> data = new ArrayList<>();
    private final Map<String, TypeDescriptor> typesData = new HashMap<>();

    public StringBuiltinUnit() {
        super("strings");
        this.initialize();
    }

    private void initialize() {
        this.typesData.put("pchar", new PCharDesriptor());
    }

    @Override
    protected List<UnitFunctionData> getSubroutines() {
        return this.data;
    }

    @Override
    protected Map<String, TypeDescriptor> getTypes() {
        return this.typesData;
    }

}
