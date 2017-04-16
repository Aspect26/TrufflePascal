package cz.cuni.mff.d3s.trupple.language.builtinunits.strings;

import cz.cuni.mff.d3s.trupple.language.builtinunits.BuiltinUnitAbstr;
import cz.cuni.mff.d3s.trupple.language.builtinunits.UnitFunctionData;
import cz.cuni.mff.d3s.trupple.parser.LexicalScope;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitFunctionData> data = new ArrayList<>();

    public StringBuiltinUnit() {
    }

    @Override
    protected List<UnitFunctionData> getIdentifiers() {
        return this.data;
    }

    @Override
    public void importTo(LexicalScope scope) {
        super.importTo(scope);
        scope.registerType("pchar", new PointerDescriptor(new PCharDesriptor()));
    }

}
