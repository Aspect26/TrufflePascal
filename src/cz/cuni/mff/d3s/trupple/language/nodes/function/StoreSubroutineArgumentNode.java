package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.SubroutineDescriptor;

// TODO: this may be removed and replaced with ReadVariableNode()
public class StoreSubroutineArgumentNode extends ExpressionNode {

    private final PascalSubroutine function;
    private final SubroutineDescriptor descriptor;

    public StoreSubroutineArgumentNode(PascalSubroutine function, SubroutineDescriptor descriptor) {
        this.function = function;
        this.descriptor = descriptor;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return function;
    }

    @Override
    public TypeDescriptor getType() {
        return descriptor;
    }

}
