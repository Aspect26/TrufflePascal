package pascal.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import pascal.language.nodes.BinaryNode;


@NodeInfo(shortName = "div")
public abstract class DivideIntegerNode extends BinaryNode{

	@Specialization(rewriteOn = ArithmeticException.class)
    protected long div(long left, long right) {
		long result = left / right;
        /*
         * The division overflows if left is Long.MIN_VALUE and right is -1.
         */
        if ((left & right & result) < 0) {
            throw new ArithmeticException("Long overflow.");
        }
        return result;
    }
	
	@Specialization(rewriteOn = ArithmeticException.class)
    protected double div(double left, long right) {
        return left / right;
    }
	
	@Specialization(rewriteOn = ArithmeticException.class)
    protected double div(long left, double right) {
        return left / right;
    }
	
	@Specialization(rewriteOn = ArithmeticException.class)
    protected double div(double left, double right) {
        return left / right;
    }
}
