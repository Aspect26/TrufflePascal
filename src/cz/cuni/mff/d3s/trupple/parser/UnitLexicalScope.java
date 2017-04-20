package cz.cuni.mff.d3s.trupple.parser;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.BlockNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;

import java.util.List;

public class UnitLexicalScope extends LexicalScope {

    private VirtualFrame frame;

    UnitLexicalScope(LexicalScope outer, String name, boolean usingTPExtension) {
        super(outer, name, usingTPExtension);
    }

    @Override
    BlockNode createInitializationBlock() {
        UnitLexicalScope outerUnitScope = (UnitLexicalScope) this.getOuterScope();
        VirtualFrame outerFrame = (outerUnitScope != null)? outerUnitScope.getFrame() : null;
        Object[] frameArguments = (outerFrame != null)? new Object[]{outerFrame} : new Object[0];

        if (this.frame == null) {
            this.frame = Truffle.getRuntime().createVirtualFrame(frameArguments, this.getFrameDescriptor());
        }

        List<StatementNode> initializationNodes = this.generateInitializationNodes(this.frame);
        initializationNodes.addAll(this.scopeInitializationNodes);

        return new BlockNode(initializationNodes.toArray(new StatementNode[initializationNodes.size()]));
    }

    VirtualFrame getFrame() {
        return this.frame;
    }
}
