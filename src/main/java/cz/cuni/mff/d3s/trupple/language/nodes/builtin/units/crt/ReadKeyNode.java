package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.crt;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.graphics.PascalGraphMode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;

public abstract class ReadKeyNode extends ExpressionNode {

    @Specialization
    public char readKey(VirtualFrame frame) {
        return PascalGraphMode.readKey();
    }

    @Override
    public TypeDescriptor getType() {
        return CharDescriptor.getInstance();
    }
}
