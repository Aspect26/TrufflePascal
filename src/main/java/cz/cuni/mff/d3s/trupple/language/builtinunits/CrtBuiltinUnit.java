package cz.cuni.mff.d3s.trupple.language.builtinunits;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.crt.DelayNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.crt.KeyPressedNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.crt.ReadKeyNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin.BuiltinProcedureDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of <i>crt</i> built-in unit. Implemented subroutines are <i>delay</i>, <i>keypressed</i> and
 * <i>readKey</i>.
 */
public class CrtBuiltinUnit extends BuiltinUnitAbstr {

    private final List<UnitSubroutineData> data = new ArrayList<>();

    public CrtBuiltinUnit() {
        this.initialize();
    }

    private void initialize() {
        data.add(new UnitSubroutineData(
                "delay",
                new BuiltinProcedureDescriptor.OneArgumentBuiltin(
                        DelayNodeGen.create(new ReadArgumentNode(0, LongDescriptor.getInstance())),
                        new FormalParameter("m", LongDescriptor.getInstance(), false)
                )
        ));
        data.add(new UnitSubroutineData(
                "keyPressed",
                new BuiltinProcedureDescriptor.NoArgumentBuiltin(KeyPressedNodeGen.create())
        ));
        data.add(new UnitSubroutineData(
                "readKey",
                new BuiltinProcedureDescriptor.NoArgumentBuiltin(ReadKeyNodeGen.create())
        ));
    }

    @Override
    protected List<UnitSubroutineData> getIdentifiers() {
        return this.data;
    }

}
