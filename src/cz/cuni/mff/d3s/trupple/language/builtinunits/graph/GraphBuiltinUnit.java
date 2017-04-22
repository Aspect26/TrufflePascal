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
                        InitGraphNodeGen.create(new ReadArgumentNode(0, LongDescriptor.getInstance()),
                                new ReadArgumentNode(1, LongDescriptor.getInstance()),
                                new ReadArgumentNode(2, StringDescriptor.getInstance())),
                        new ArrayList<FormalParameter>(){{
                            new FormalParameter("graphMode", LongDescriptor.getInstance(), false);
                            new FormalParameter("graphDriver", LongDescriptor.getInstance(), false);
                            new FormalParameter("pathToDriver", StringDescriptor.getInstance(), false);
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
                        PutPixelNodeGen.create(new ReadArgumentNode(0, LongDescriptor.getInstance()),
                                new ReadArgumentNode(1, LongDescriptor.getInstance()),
                                new ReadArgumentNode(2, LongDescriptor.getInstance())),
                        new ArrayList<FormalParameter>(){{
                            new FormalParameter("x", LongDescriptor.getInstance(), false);
                            new FormalParameter("y", LongDescriptor.getInstance(), false);
                            new FormalParameter("color", LongDescriptor.getInstance(), false);
                        }}
                )

        ));

    }

    @Override
    protected List<UnitSubroutineData> getIdentifiers() {
        return this.data;
    }

}
