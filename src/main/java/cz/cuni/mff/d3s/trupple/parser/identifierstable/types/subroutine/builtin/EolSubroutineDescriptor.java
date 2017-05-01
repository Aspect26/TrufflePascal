package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.file.EolBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;

public class EolSubroutineDescriptor extends BuiltinFunctionDescriptor.OneArgumentBuiltin {

    public EolSubroutineDescriptor() {
        super(EolBuiltinNodeGen.create(new ExpressionNode[]{new ReadAllArgumentsNode()}),
                new FormalParameter("p", new FileDescriptor(null), false));
    }

}