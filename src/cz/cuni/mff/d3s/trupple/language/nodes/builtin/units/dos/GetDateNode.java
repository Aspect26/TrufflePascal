package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.dos;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

import java.time.LocalDateTime;

/**
 * Official specification:
 * GetDate returns the system's date. Year is a number in the range 1980..2099. Month is the day of the month, weekday
 * is the day of the week, starting with Sunday as day 0.
 *
 * Differences:
 * Year is not a range but can be any integer
 */
@NodeChildren({ @NodeChild(type = ExpressionNode.class), @NodeChild(type = ExpressionNode.class),
        @NodeChild(type = ExpressionNode.class), @NodeChild(type = ExpressionNode.class) })
public abstract class GetDateNode extends StatementNode {

    @Specialization
    void getDate(Reference yearReference, Reference monthReference, Reference dayReference, Reference weekDayReference) {
        LocalDateTime now = LocalDateTime.now();

        this.setLongValue(yearReference, now.getYear());
        this.setLongValue(monthReference, now.getMonthValue());
        this.setLongValue(dayReference, now.getDayOfMonth());
        this.setLongValue(weekDayReference, now.getDayOfWeek().getValue() % 7);  // by specification, Sunday shall be 0, not 7 in Pascal
    }

    private void setLongValue(Reference reference, long value) {
        reference.getFromFrame().setLong(reference.getFrameSlot(), value);
    }

}
