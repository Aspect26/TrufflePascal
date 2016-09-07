package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "random")
public abstract class RandomBuiltinNode extends BuiltinNode {

	@Specialization
	public long getRandom(long bound) {
		return this.getContext().getRandom(bound);
	}
}
