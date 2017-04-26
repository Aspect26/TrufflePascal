package cz.cuni.mff.d3s.trupple.language.builtinunits.dos;

import cz.cuni.mff.d3s.trupple.language.builtinunits.BuiltinUnitAbstr;
import cz.cuni.mff.d3s.trupple.language.builtinunits.UnitSubroutineData;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos.ExecNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos.GetDateNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos.GetMsCountNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos.GetTimeNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.StringDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.BuiltinProcedureDescriptor;

import java.util.ArrayList;
import java.util.List;

public class DosBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitSubroutineData> data = new ArrayList<>();

    public DosBuiltinUnit() {
        this.initialize();
    }

    private void initialize() {
        // TODO: the subroutine name can be directly in the node
        data.add(new UnitSubroutineData(
                "exec",
                new BuiltinProcedureDescriptor.NoReferenceParameterBuiltin(
                        ExecNodeGen.create(new ReadArgumentNode(0, StringDescriptor.getInstance()), new ReadArgumentNode(1, StringDescriptor.getInstance())),
                        new ArrayList<FormalParameter>() {{
                            add(new FormalParameter("i", StringDescriptor.getInstance(), false));
                            add(new FormalParameter("j", StringDescriptor.getInstance(), false));
                        }})
                )
        );
        data.add(new UnitSubroutineData(
                "getDate",
                new BuiltinProcedureDescriptor.FullReferenceParameterBuiltin(
                        GetDateNodeGen.create(new ReadArgumentNode(0, LongDescriptor.getInstance()), new ReadArgumentNode(1, LongDescriptor.getInstance()),
                                new ReadArgumentNode(2, LongDescriptor.getInstance()), new ReadArgumentNode(3, LongDescriptor.getInstance())),
                        new ArrayList<FormalParameter>(){{
                            add(new FormalParameter("y", LongDescriptor.getInstance(), true));
                            add(new FormalParameter("m", LongDescriptor.getInstance(), true));
                            add(new FormalParameter("d", LongDescriptor.getInstance(), true));
                            add(new FormalParameter("wd", LongDescriptor.getInstance(), true));
                        }}
                        )
                )

        );
        data.add(new UnitSubroutineData(
                "getMsCount",
                new BuiltinProcedureDescriptor.NoArgumentBuiltin(GetMsCountNodeGen.create())
                )
        );
        data.add(new UnitSubroutineData(
                        "getTime",
                        new BuiltinProcedureDescriptor.FullReferenceParameterBuiltin(
                                GetTimeNodeGen.create(new ReadArgumentNode(0, LongDescriptor.getInstance()), new ReadArgumentNode(1, LongDescriptor.getInstance()),
                                        new ReadArgumentNode(2, LongDescriptor.getInstance()), new ReadArgumentNode(3, LongDescriptor.getInstance())),
                                new ArrayList<FormalParameter>(){{
                                    add(new FormalParameter("h", LongDescriptor.getInstance(), true));
                                    add(new FormalParameter("m", LongDescriptor.getInstance(), true));
                                    add(new FormalParameter("s", LongDescriptor.getInstance(), true));
                                    add(new FormalParameter("n", LongDescriptor.getInstance(), true));
                                }}
                        )
                )

        );
    }

    @Override
    protected List<UnitSubroutineData> getIdentifiers() {
        return this.data;
    }

}
