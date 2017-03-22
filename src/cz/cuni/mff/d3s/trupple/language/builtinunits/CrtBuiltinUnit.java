package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.crt.DelayNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.BuiltinProcedureDescriptor;

import java.util.ArrayList;
import java.util.List;

public class CrtBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitFunctionData> data = new ArrayList<>();

    public CrtBuiltinUnit() {
        this.initialize();
    }

    private void initialize() {
        data.add(new UnitFunctionData(
                "delay",
                new BuiltinProcedureDescriptor.OneArgumentBuiltin(),
                DelayNodeGen.create(new ReadArgumentNode(0)))
        );
    }

    @Override
    protected List<UnitFunctionData> getIdentifiers() {
        return this.data;
    }

}
