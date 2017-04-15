package cz.cuni.mff.d3s.trupple.language.builtinunits.graph;

import cz.cuni.mff.d3s.trupple.language.builtinunits.BuiltinUnitAbstr;
import cz.cuni.mff.d3s.trupple.language.builtinunits.UnitFunctionData;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph.CloseGraphNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph.InitGraphNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph.PutPixelNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.BuiltinProcedureDescriptor;

import java.util.ArrayList;
import java.util.List;

public class GraphBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitFunctionData> data = new ArrayList<>();

    public GraphBuiltinUnit() {
        this.init();
    }

    private void init() {
        this.data.add(new UnitFunctionData(
                "InitGraph",
                new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin(),
                InitGraphNodeGen.create(new ReadArgumentNode(0), new ReadArgumentNode(1),
                        new ReadArgumentNode(2))
        ));

        this.data.add(new UnitFunctionData(
                "CloseGraph",
                new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin(),
                CloseGraphNodeGen.create()
        ));

        this.data.add(new UnitFunctionData(
                "PutPixel",
                new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin(),
                PutPixelNodeGen.create(new ReadArgumentNode(0), new ReadArgumentNode(1),
                        new ReadArgumentNode(2))
        ));

    }

    @Override
    protected List<UnitFunctionData> getIdentifiers() {
        return this.data;
    }

}
