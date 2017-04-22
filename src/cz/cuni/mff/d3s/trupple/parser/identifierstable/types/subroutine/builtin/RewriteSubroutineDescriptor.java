package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.file.RewriteBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;


public class RewriteSubroutineDescriptor extends BuiltinProcedureDescriptor.OneArgumentBuiltin {

    public RewriteSubroutineDescriptor() {
        super(RewriteBuiltinNodeGen.create(new ReadArgumentNode(0, new FileDescriptor(null))),
                new FormalParameter("p", new FileDescriptor(null), false));
    }

}