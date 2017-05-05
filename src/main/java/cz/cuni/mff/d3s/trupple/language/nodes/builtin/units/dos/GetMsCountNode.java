package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

/**
 * Official specification:
 * GetMSCount returns a number of milliseconds elapsed since a certain moment in time. This moment in time is
 * implementation dependent. This function is used for timing purposes: Substracting the results of 2 subsequent calls
 * to this function returns the number of milliseconds elapsed between the two calls.
 *
 * This call is not very reliable, it is recommended to use some system specific calls for timings.
 *
 * Differences:
 * Our implementation uses Java's System.nanoTime() which is very precise in contrast with the specification of the
 * official Pascal function.
 */
public abstract class GetMsCountNode extends ExpressionNode {

    @Specialization
    long getMiliseconds(VirtualFrame frame) {
        return System.nanoTime() / (1024 * 1024);
    }

    @Override
    public TypeDescriptor getType() {
        return LongDescriptor.getInstance();
    }

}
