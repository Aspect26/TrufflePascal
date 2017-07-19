package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.string.StrAllocNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.UnitLexicalScope;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.PointerDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.BuiltinFunctionDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of <i>strings</i> built-in unit. Implemented subroutines are <i>StrAlloc</i> and some other are
 * implemented in a Pascal unit source.
 */
public class StringBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitSubroutineData> data = new ArrayList<>();

    public StringBuiltinUnit() {
        this.data.add(new UnitSubroutineData(
           "StrAlloc",
                new BuiltinFunctionDescriptor.OneArgumentBuiltin(
                        StrAllocNodeGen.create(new ReadArgumentNode(0, LongDescriptor.getInstance())),
                        new FormalParameter("size", LongDescriptor.getInstance(), false)
                )
        ));
    }

    @Override
    protected List<UnitSubroutineData> getIdentifiers() {
        return this.data;
    }

    /**
     * {@inheritDoc} It also imports PChar type ({@link PCharDesriptor}) to the scope.
     */
    @Override
    public void importTo(UnitLexicalScope scope) {
        super.importTo(scope);
        try {
            scope.registerType("pchar", new PointerDescriptor(PCharDesriptor.getInstance()));
        } catch (LexicalException e) {
            throw new PascalRuntimeException("Could not import string unit: " + e.getMessage());
        }
        scope.markAllIdentifiersPublic();
    }

}
