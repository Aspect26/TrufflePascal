package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.crt;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.graphics.PascalGraphMode;

public abstract class KeyPressedNode extends ExpressionNode {

    @Specialization
    boolean keyPressed(VirtualFrame frame) {
        return PascalGraphMode.keyPressed();
    }

}
