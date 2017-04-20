package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.file.EolBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

public class EolSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public EolSubroutineDescriptor() {
        super(EolBuiltinNodeFactory.create(new ExpressionNode[]{new ReadAllArgumentsNode()}),
                new FormalParameter("p", new FileDescriptor(null), false),
                BooleanDescriptor.getInstance());
    }

}