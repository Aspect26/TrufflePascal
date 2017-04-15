package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.graphics.PascalGraphMode;

@NodeChildren({
        @NodeChild(type = ExpressionNode.class),
        @NodeChild(type = ExpressionNode.class),
        @NodeChild(type = ExpressionNode.class)
})
public abstract class PutPixelNode extends ExpressionNode {

    @Specialization
    public long putPixel(long x, long y, long color) {
        return PascalGraphMode.drawPixel((int) x, (int) y, (int) color);
    }

}
