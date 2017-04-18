package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.file.RewriteBuiltinNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadAllArgumentsNode;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;

import java.util.ArrayList;

public class RewriteSubroutineDescriptor extends BuiltinProcedureDescriptor.OneArgumentBuiltin {

    public RewriteSubroutineDescriptor() {
        super(RewriteBuiltinNodeFactory.create(new ReadArgumentNode(0)),
                new FormalParameter("p", new FileDescriptor(null), false));
    }

}