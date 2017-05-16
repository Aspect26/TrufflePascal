package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

/**
 * The node for invoking a subroutine call.
 */
@NodeInfo(shortName = "invoke")
public abstract class InvokeNode extends ExpressionNode {

    private final FrameSlot subroutineSlot;
    private final TypeDescriptor type;
    @Children private final ExpressionNode[] argumentNodes;
    @CompilerDirectives.CompilationFinal private PascalSubroutine subroutine;

	InvokeNode(FrameSlot subroutineSlot, ExpressionNode[] argumentNodes, TypeDescriptor type) {
		this.subroutineSlot = subroutineSlot;
	    this.argumentNodes = argumentNodes;
	    this.type = type;
	}

	// TODO: type specializations

	@Specialization
	Object invoke(VirtualFrame frame) {
	    if (subroutine == null) {
	        CompilerDirectives.transferToInterpreterAndInvalidate();
	        subroutine = this.getMySubroutine(frame);
        }
        Object[] argumentValues = this.evaluateArguments(frame);

        return subroutine.getCallTarget().call(argumentValues);
	}

	@Override
    public TypeDescriptor getType() {
	    return this.type;
    }

    private PascalSubroutine getMySubroutine(VirtualFrame frame) {
	    VirtualFrame frameContainingSubroutine = this.getFrameContainingSlot(frame, this.subroutineSlot);
        return (PascalSubroutine) frameContainingSubroutine.getValue(this.subroutineSlot);
    }

    @ExplodeLoop
    private Object[] evaluateArguments(VirtualFrame frame) {
        Object[] argumentValues = new Object[argumentNodes.length + 1];
        argumentValues[0] = frame;
        for (int i = 0; i < argumentNodes.length; i++) {
            argumentValues[i+1] = argumentNodes[i].executeGeneric(frame);
        }

        return argumentValues;
    }
}
