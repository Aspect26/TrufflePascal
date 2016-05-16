package pascal.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.BinaryNode;


@NodeInfo(shortName = "mod")
public abstract class ModuloNode extends BinaryNode{

	@Specialization(rewriteOn = ArithmeticException.class)
    protected long add(long left, long right) {
		long result = left % right;
		return result;
    }
}
