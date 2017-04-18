package cz.cuni.mff.d3s.trupple.language.nodes.literals;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.PascalRuntimeException;

@NodeInfo(shortName = "func")
public final class FunctionLiteralNode extends ExpressionNode {
	private final FrameSlot subroutineSlot;

	public FunctionLiteralNode(FrameSlot subroutineSlot) {
		this.subroutineSlot = subroutineSlot;
	}

	@Override
	public PascalSubroutine executeGeneric(VirtualFrame frame) {
        VirtualFrame currentFrame = frame;
		PascalSubroutine function = null;
		while(currentFrame != null){
            try {
                if (!currentFrame.getFrameDescriptor().getSlots().contains(this.subroutineSlot)) {
                    currentFrame = (VirtualFrame) currentFrame.getArguments()[0];
                } else {
                    function = (PascalSubroutine) currentFrame.getObject(this.subroutineSlot);
                    break;
                }
            } catch (FrameSlotTypeException | ClassCastException e) {
                throw new PascalRuntimeException("Not a function.");
            }
		}
		
		if(function == null) {
            throw new PascalRuntimeException("Called function does not exist.");
        }
		
		return function;
	}

}
