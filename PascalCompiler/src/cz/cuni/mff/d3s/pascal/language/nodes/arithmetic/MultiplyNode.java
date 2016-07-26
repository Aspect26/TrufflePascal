package cz.cuni.mff.d3s.pascal.language.nodes.arithmetic;

import com.oracle.truffle.api.ExactMath;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.pascal.language.nodes.BinaryNode;


@NodeInfo(shortName = "*")
public abstract class MultiplyNode extends BinaryNode{

	@Specialization(rewriteOn = ArithmeticException.class)
    protected long mul(long left, long right) {
        return ExactMath.multiplyExact(left, right);
    }
	
	@Specialization(rewriteOn = ArithmeticException.class)
    protected double mul(double left, long right) {
        return left * right;
    }
	
	@Specialization(rewriteOn = ArithmeticException.class)
    protected double mul(long left, double right) {
        return left * right;
    }
	
	@Specialization(rewriteOn = ArithmeticException.class)
    protected double mul(double left, double right) {
        return left * right;
    }
}
