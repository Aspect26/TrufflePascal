package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos.ExecNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos.GetDateNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos.GetMsCountNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos.GetTimeNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.BuiltinProcedureDescriptor;

import java.util.ArrayList;
import java.util.List;

public class DosBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitFunctionData> data = new ArrayList<>();

    public DosBuiltinUnit() {
        this.initialize();
    }

    private void initialize() {
        // TODO: the subroutine name can be directly in the node
        data.add(new UnitFunctionData(
                "exec",
                new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin(),
                ExecNodeGen.create(new ReadArgumentNode(0), new ReadArgumentNode(1)))
        );
        data.add(new UnitFunctionData(
                "getDate",
                new BuiltinProcedureDescriptor.FullReferenceParameterBuiltin(),
                GetDateNodeGen.create(new ReadArgumentNode(0), new ReadArgumentNode(1),
                        new ReadArgumentNode(2), new ReadArgumentNode(3)))
        );
        data.add(new UnitFunctionData(
                "getMsCount",
                new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin(),
                GetMsCountNodeGen.create())
        );
        data.add(new UnitFunctionData(
                "getTime",
                new BuiltinProcedureDescriptor.FullReferenceParameterBuiltin(),
                GetTimeNodeGen.create(new ReadArgumentNode(0), new ReadArgumentNode(1),
                        new ReadArgumentNode(2), new ReadArgumentNode(3)))
        );
    }

    @Override
    protected List<UnitFunctionData> getIdentifiers() {
        return this.data;
    }

}
