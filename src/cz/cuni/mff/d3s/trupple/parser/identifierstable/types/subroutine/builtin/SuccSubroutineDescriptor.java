package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ordinal.SuccBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.GenericEnumTypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;

public class SuccSubroutineDescriptor extends OverloadedFunctionDescriptor {

    public SuccSubroutineDescriptor() {
        this.addOverLoad(new BuiltinFunctionDescriptor.OrdinalArgumentBuiltin(
                SuccBuiltinNodeFactory.create(new ReadArgumentNode(0, LongDescriptor.getInstance())),
                new FormalParameter("i", LongDescriptor.getInstance(), false)
        ));

        this.addOverLoad(new BuiltinFunctionDescriptor.OrdinalArgumentBuiltin(
                SuccBuiltinNodeFactory.create(new ReadArgumentNode(0, CharDescriptor.getInstance())),
                new FormalParameter("c", CharDescriptor.getInstance(), false)
        ));

        this.addOverLoad(new BuiltinFunctionDescriptor.OrdinalArgumentBuiltin(
                SuccBuiltinNodeFactory.create(new ReadArgumentNode(0, BooleanDescriptor.getInstance())),
                new FormalParameter("c", BooleanDescriptor.getInstance(), false)
        ));

        this.addOverLoad(new BuiltinFunctionDescriptor.OrdinalArgumentBuiltin(
                SuccBuiltinNodeFactory.create(new ReadArgumentNode(0, GenericEnumTypeDescriptor.getInstance())),
                new FormalParameter("c", GenericEnumTypeDescriptor.getInstance(), false)
        ));

    }

}