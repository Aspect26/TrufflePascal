package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp.AssignBuiltinNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.ReadArgumentNode;
import cz.cuni.mff.d3s.trupple.parser.utils.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.FileDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.StringDescriptor;

import java.util.ArrayList;

public class AssignSubroutineDescriptor extends BuiltinProcedureDescriptor {

    public AssignSubroutineDescriptor() {
        super(AssignBuiltinNodeGen.create(new ReadArgumentNode(0, new FileDescriptor(null)), new ReadArgumentNode(1, StringDescriptor.getInstance())),
                new ArrayList<FormalParameter>(){{
                    add(new FormalParameter("f", new FileDescriptor(null), false));
                    add(new FormalParameter("f", StringDescriptor.getInstance(), false));
                }});
    }

}
