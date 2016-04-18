package pascal.language.nodes.controlflow;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.api.profiles.ConditionProfile;

import pascal.language.nodes.PascalNode;

public class IfNode extends PascalNode {

	@Child private PascalNode conditionNode;
	@Child private PascalNode thenNode;
	@Child private PascalNode elseNode;
	
	private final ConditionProfile conditionProfile =
            ConditionProfile.createBinaryProfile();
	
	public IfNode(PascalNode conditionNode, PascalNode thenNode, PascalNode elseNode){
		this.conditionNode = conditionNode;
		this.thenNode = thenNode;
		this.elseNode = elseNode;
	}
	
	@Override
	public Object execute(VirtualFrame virtualFrame) {
		if(this.conditionProfile.profile(this.testCondition(virtualFrame))){
			return thenNode.execute(virtualFrame);
		} else{
			return elseNode.execute(virtualFrame);
		}
	}
	
	private boolean testCondition(VirtualFrame virtualFrame){
		try{
			return this.conditionNode.executeBoolean(virtualFrame);
		} catch(UnexpectedResultException e){
			return false; // SemErr?
		}
	}

}
