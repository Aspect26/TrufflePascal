package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryNode;

@NodeInfo(shortName = "=")
public abstract class EqualsNode extends BinaryNode {

	@Override
	public abstract boolean executeBoolean(VirtualFrame frame);

	@Specialization
	protected boolean equals(boolean left, boolean right) {
		return left == right;
	}

	@Specialization
	protected boolean equals(long left, long right) {
		return left == right;
	}

	@Specialization
    protected boolean equals(char left, char right) {
	    return left == right;
    }

	@Specialization
	protected boolean equals(EnumValue left, EnumValue right) { return left.getValue().equals(right.getValue()); }

	@Specialization
	protected boolean equals(SetTypeValue left, SetTypeValue right) {
	    return left.equals(right);
    }

    @Specialization
    protected boolean equals(PointerValue left, PointerValue right) {
	    return left.equals(right);
    }
}
