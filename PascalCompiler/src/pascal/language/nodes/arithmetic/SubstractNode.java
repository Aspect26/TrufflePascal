package pascal.language.nodes.arithmetic;

import com.oracle.truffle.api.ExactMath;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.BinaryNode;


@NodeInfo(shortName = "-")
public abstract class SubstractNode extends BinaryNode{

	@Specialization(rewriteOn = ArithmeticException.class)
    protected long add(long left, long right) {
        return ExactMath.subtractExact(left, right);
    }
}
