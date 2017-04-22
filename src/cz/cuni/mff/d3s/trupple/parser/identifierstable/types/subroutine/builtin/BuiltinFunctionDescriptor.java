package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.builtin;

import com.oracle.truffle.api.frame.FrameDescriptor;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.FunctionPascalRootNode;
import cz.cuni.mff.d3s.trupple.parser.FormalParameter;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.subroutine.FunctionDescriptor;

import java.util.Collections;
import java.util.List;

public abstract class BuiltinFunctionDescriptor extends FunctionDescriptor {

    BuiltinFunctionDescriptor(ExpressionNode bodyNode, List<FormalParameter> parameters) {
        super(parameters, bodyNode.getType());
        this.setRootNode(new FunctionPascalRootNode(new FrameDescriptor(), bodyNode));
    }

    @Override
    public boolean isSubroutineParameter(int index) {
        return false;
    }

    public static class OneArgumentBuiltin extends BuiltinFunctionDescriptor {

        public OneArgumentBuiltin(ExpressionNode bodyNode, FormalParameter parameter) {
            super(bodyNode, Collections.singletonList(parameter));
        }

        @Override
        public boolean hasParameters() {
            return true;
        }

    }

}
