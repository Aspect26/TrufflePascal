package cz.cuni.mff.d3s.trupple.parser;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.BlockNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class UnitLexicalScope extends LexicalScope {

    private VirtualFrame frame;
    private final Set<String> publicIdentifiers;

    UnitLexicalScope(LexicalScope outer, String name, boolean usingTPExtension) {
        super(outer, name, usingTPExtension);
        this.publicIdentifiers = new HashSet<>();
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

    @Override
    boolean containsPublicIdentifier(String identifier) {
        return this.publicIdentifiers.contains(identifier);
    }

    void markAllIdentifiersPublic() {
        Map<String, TypeDescriptor> allIdentifiers = this.localIdentifiers.getAllIdentifiers();
        for (Map.Entry<String, TypeDescriptor> entry : allIdentifiers.entrySet()) {
            String currentIdentifier = entry.getKey();
            this.publicIdentifiers.add(currentIdentifier);
        }
    }

}
