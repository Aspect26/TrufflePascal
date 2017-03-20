package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "mod")
public abstract class ModuloNode extends BinaryNode {

	@Specialization
	protected long mod(long left, long right) {
		return left % right;
	}
}
