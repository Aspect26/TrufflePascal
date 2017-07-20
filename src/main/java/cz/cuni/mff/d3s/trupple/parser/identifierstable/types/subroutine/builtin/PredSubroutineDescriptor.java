package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal.PredBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.GenericEnumTypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.IntDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

/**
 * Type descriptor for Pascal's <i>pred</i> built-in subroutine.
 */
public class PredSubroutineDescriptor extends OverloadedFunctionDescriptor {

    /**
     * The default constructor which automatically registers the supported overloads of this subroutine.
     */
    public PredSubroutineDescriptor() {
        this.addOverLoad(new BuiltinFunctionDescriptor.OrdinalArgumentBuiltin(
                PredBuiltinNodeGen.create(new ReadArgumentNode(0, IntDescriptor.getInstance())),
                new FormalParameter("i", IntDescriptor.getInstance(), false)
        ));

        this.addOverLoad(new BuiltinFunctionDescriptor.OrdinalArgumentBuiltin(
                PredBuiltinNodeGen.create(new ReadArgumentNode(0, LongDescriptor.getInstance())),
                new FormalParameter("i", LongDescriptor.getInstance(), false)
        ));

        this.addOverLoad(new BuiltinFunctionDescriptor.OrdinalArgumentBuiltin(
                PredBuiltinNodeGen.create(new ReadArgumentNode(0, CharDescriptor.getInstance())),
                new FormalParameter("c", CharDescriptor.getInstance(), false)
        ));

        this.addOverLoad(new BuiltinFunctionDescriptor.OrdinalArgumentBuiltin(
                PredBuiltinNodeGen.create(new ReadArgumentNode(0, BooleanDescriptor.getInstance())),
                new FormalParameter("c", BooleanDescriptor.getInstance(), false)
        ));

        this.addOverLoad(new BuiltinFunctionDescriptor.OrdinalArgumentBuiltin(
                PredBuiltinNodeGen.create(new ReadArgumentNode(0, GenericEnumTypeDescriptor.getInstance())),
                new FormalParameter("c", GenericEnumTypeDescriptor.getInstance(), false)
        ));

    }

}
