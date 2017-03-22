package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.crt;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.sun.media.jfxmedia.logging.Logger;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

/**
 * Official specification:
 * Delay waits a specified number of milliseconds. The number of specified seconds is an approximation, and may be off
 * a lot, if system load is high.
 *
 * Differences:
 * None. Note that the implementation is also not precise.
 */
@NodeChild(type = ExpressionNode.class)
public abstract class DelayNode extends ExpressionNode {

    @Specialization
    long delay(long miliSeconds) {
        try {
            Thread.sleep(miliSeconds);
        } catch (InterruptedException e) {
            Logger.logMsg(Logger.INFO, "Delay was interrupted.");
        }

        // Note that this is a procedure, so it doesn't depend on what it returns
        return miliSeconds;
    }

}
