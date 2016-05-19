package pascal.language.nodes.arithmetic;

import com.oracle.truffle.api.ExactMath;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.BinaryNode;


@NodeInfo(shortName = "*")
public abstract class MultiplyNode extends BinaryNode{

	@Specialization(rewriteOn = ArithmeticException.class)
    protected long mul(long left, long right) {
        return ExactMath.multiplyExact(left, right);
    }
	
	@Specialization(rewriteOn = ArithmeticException.class)
    protected int mul(int left, int right) {
        return ExactMath.multiplyExact(left, right);
    }
}
