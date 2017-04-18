package cz.cuni.mff.d3s.trupple.language.builtinunits.graph;

import cz.cuni.mff.d3s.trupple.language.builtinunits.BuiltinUnitAbstr;
import cz.cuni.mff.d3s.trupple.language.builtinunits.UnitSubroutineData;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph.CloseGraphNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph.InitGraphNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph.PutPixelNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.StringDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.BuiltinProcedureDescriptor;

import java.util.ArrayList;
import java.util.List;

public class GraphBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitSubroutineData> data = new ArrayList<>();

    public GraphBuiltinUnit() {
        this.init();
    }

    private void init() {
        this.data.add(new UnitSubroutineData(
                "InitGraph",
                new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin(
                        InitGraphNodeGen.create(new ReadArgumentNode(0), new ReadArgumentNode(1), new ReadArgumentNode(2)),
                        new ArrayList<FormalParameter>(){{
                            new FormalParameter("graphMode", new LongDescriptor(), false);
                            new FormalParameter("graphDriver", new LongDescriptor(), false);
                            new FormalParameter("pathToDriver", new StringDescriptor(), false);
                        }}
                )
        ));

        this.data.add(new UnitSubroutineData(
                "CloseGraph",
                new BuiltinProcedureDescriptor.NoArgumentBuiltin(CloseGraphNodeGen.create())
        ));

        this.data.add(new UnitSubroutineData(
                "PutPixel",
                new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin(
                        PutPixelNodeGen.create(new ReadArgumentNode(0), new ReadArgumentNode(1), new ReadArgumentNode(2)),
                        new ArrayList<FormalParameter>(){{
                            new FormalParameter("x", new LongDescriptor(), false);
                            new FormalParameter("y", new LongDescriptor(), false);
                            new FormalParameter("color", new LongDescriptor(), false);
                        }}
                )

        ));

    }

    @Override
    protected List<UnitSubroutineData> getIdentifiers() {
        return this.data;
    }

}
