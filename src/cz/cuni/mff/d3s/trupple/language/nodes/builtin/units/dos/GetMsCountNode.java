package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

/**
 * Official specification:
 * GetMSCount returns a number of milliseconds elapsed since a certain moment in time. This moment in time is
 * implementation dependent. This function is used for timing purposes: Substracting the results of 2 subsequent calls
 * to this function returns the number of milliseconds elapsed between the two calls.
 *
 * This call is not very reliable, it is recommended to use some system specific calls for timings.
 *
 * Differences:
 * None. Note that the System.currentTimeMillis function is used so it won't be too precise, as stated in specification :)
 */
public abstract class GetMsCountNode extends ExpressionNode {

    @Specialization
    long getMiliseconds(VirtualFrame frame) {
        return System.currentTimeMillis();
    }

}
