package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.file.EofBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

public class EofSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public EofSubroutineDescriptor() {
        super(EofBuiltinNodeFactory.create(new ExpressionNode[]{new ReadAllArgumentsNode()}),
                new FormalParameter("p", new FileDescriptor(null), false),
                new BooleanDescriptor());
    }

}