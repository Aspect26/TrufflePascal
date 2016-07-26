package cz.cuni.mff.d3s.pascal.language.nodes.arithmetic;

import com.oracle.truffle.api.ExactMath;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.pascal.language.nodes.BinaryNode;


@NodeInfo(shortName = "-")
public abstract class SubstractNode extends BinaryNode{

	@Specialization(rewriteOn = ArithmeticException.class)
    protected long sub(long left, long right) {
        return ExactMath.subtractExact(left, right);
    }
	
	@Specialization(rewriteOn = ArithmeticException.class)
    protected double sub(double left, long right) {
        return left - right;
    }
	
	protected double sub(long left, double right) {
        return left - right;
    }
	
	protected double sub(double left, double right) {
        return left - right;
    }
}
