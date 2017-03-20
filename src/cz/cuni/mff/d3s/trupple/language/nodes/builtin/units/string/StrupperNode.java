package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.string;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

@NodeChild(type = ExpressionNode.class)
public abstract class StrupperNode extends ExpressionNode {

    @Specialization
    String toUpper(Reference stringReference) {
        try {
            String originalValue = (String) stringReference.getFromFrame().getObject(stringReference.getFrameSlot());
            String newValue = originalValue.toUpperCase();
            stringReference.getFromFrame().setObject(stringReference.getFrameSlot(), newValue);
            return originalValue;
        } catch (FrameSlotTypeException e) {
            throw new RuntimeException("Wrong type passed to strupper");
        }
    }

}
