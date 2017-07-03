package cz.cuni.mff.d3s.trupple.language.nodes.builtin.tp;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

/**
 * Node representing Turbo Pascal's randomize procedure. It resets actual random seed.
 */
@NodeInfo(shortName = "randomize")
public class RandomizeBuiltinNode extends StatementNode {
	
    @Override
    public void executeVoid(VirtualFrame frame) {
        PascalLanguage.INSTANCE.randomize();
    }

}
