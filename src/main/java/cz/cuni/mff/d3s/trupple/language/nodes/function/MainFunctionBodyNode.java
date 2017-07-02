package cz.cuni.mff.d3s.trupple.language.nodes.function;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.NodeFields;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.interop.MainFunctionObject;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.root.FunctionPascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;


@NodeFields({
    @NodeField(name = "slot", type = FrameSlot.class),
})
public abstract class MainFunctionBodyNode extends ExpressionNode {

	private final StatementNode body;
	private final FrameDescriptor frameDescriptor;

    protected abstract FrameSlot getSlot();

	MainFunctionBodyNode(StatementNode body, FrameDescriptor frameDescriptor) {
        this.body = body;
        this.frameDescriptor = frameDescriptor;
    }

	@Specialization
    Object execute(VirtualFrame frame) {
        FunctionBodyNode bodyNode = FunctionBodyNodeGen.create(body, this.getSlot(), null);
	    return new MainFunctionObject(new FunctionPascalRootNode(frameDescriptor, bodyNode));
    }

    @Override
    public TypeDescriptor getType() {
        return null;
    }

}
