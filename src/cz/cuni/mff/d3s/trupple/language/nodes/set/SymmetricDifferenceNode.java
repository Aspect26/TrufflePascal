package cz.cuni.mff.d3s.trupple.language.nodes.set;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryNode;

@NodeInfo(shortName = "><")
public abstract class SymmetricDifferenceNode extends BinaryNode {

	@Override
	public abstract Object executeGeneric(VirtualFrame frame);

	@Specialization
	protected SetTypeValue diff(SetTypeValue left, SetTypeValue right) {
		return SetTypeValue.symmetricDifference(left, right);
	}
}
