package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.graphics.PascalGraphMode;

/**
 * Node representing graph' putpixel subroutine.
 *
 * This node uses specializations which means that it is not used directly but completed node is generated by Truffle.
 * {@link PutPixelNodeGen}
 */
@NodeChildren({
        @NodeChild(type = ExpressionNode.class),
        @NodeChild(type = ExpressionNode.class),
        @NodeChild(type = ExpressionNode.class)
})
public abstract class PutPixelNode extends StatementNode {

    @Specialization
    void putPixel(long x, long y, long color) {
        PascalGraphMode.drawPixel((int) x, (int) y, (int) color);
    }

}
