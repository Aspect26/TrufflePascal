package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

import java.time.LocalDateTime;

/**
 * Official specification:
 * GetTime returns the system's time. Hour is a on a 24-hour time scale. sec100 is in hundredth of a second.
 *
 * Differences:
 * None.
 */
@NodeChildren({ @NodeChild(type = ExpressionNode.class), @NodeChild(type = ExpressionNode.class),
        @NodeChild(type = ExpressionNode.class), @NodeChild(type = ExpressionNode.class) })
public abstract class GetTimeNode extends ExpressionNode {

    @Specialization
    int getTime(Reference hourReference, Reference minuteReference, Reference secondReference, Reference sec100Reference) {
        LocalDateTime now = LocalDateTime.now();

        this.setLongValue(hourReference, now.getHour());
        this.setLongValue(minuteReference, now.getMinute());
        this.setLongValue(secondReference, now.getSecond());
        this.setLongValue(sec100Reference, now.getNano() / 1000 / 1000 / 10);  // by specification, Sunday shall be 0, not 7 in Pascal

        // NOTE: it is a procedure, so it shouldn't return anything
        return 0;
    }

    private void setLongValue(Reference reference, long value) {
        reference.getFromFrame().setLong(reference.getFrameSlot(), value);
    }

}
