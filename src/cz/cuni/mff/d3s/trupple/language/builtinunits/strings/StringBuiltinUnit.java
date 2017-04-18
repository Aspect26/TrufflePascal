package cz.cuni.mff.d3s.trupple.language.builtinunits.strings;

import cz.cuni.mff.d3s.trupple.language.builtinunits.BuiltinUnitAbstr;
import cz.cuni.mff.d3s.trupple.language.builtinunits.UnitSubroutineData;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.string.StrAllocNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.LexicalScope;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.BuiltinFunctionDescriptor;

import java.util.ArrayList;
import java.util.List;

public class StringBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitSubroutineData> data = new ArrayList<>();

    public StringBuiltinUnit() {
        this.data.add(new UnitSubroutineData(
           "StrAlloc",
                new BuiltinFunctionDescriptor.OneArgumentBuiltin(
                        StrAllocNodeGen.create(new ReadArgumentNode(0)),
                        new FormalParameter("size", new LongDescriptor(), false),
                        new PointerDescriptor("")
                )
        ));
    }

    @Override
    protected List<UnitSubroutineData> getIdentifiers() {
        return this.data;
    }

    @Override
    public void importTo(LexicalScope scope) {
        super.importTo(scope);
        scope.registerType("pchar", new PointerDescriptor(new PCharDesriptor()));
    }

}
