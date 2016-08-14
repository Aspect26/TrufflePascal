package cz.cuni.mff.d3s.trupple.language.nodes;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class InitializationNode extends StatementNode{

	private FrameSlot frameSlot;
	private Object object;
	
	public InitializationNode(FrameSlot frameSlot, Object object) {
		this.frameSlot = frameSlot;
		this.object = object;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		frame.setObject(frameSlot, object);
	}

}
