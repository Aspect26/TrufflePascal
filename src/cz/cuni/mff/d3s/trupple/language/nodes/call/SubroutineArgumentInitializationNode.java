package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunction;

public class SubroutineArgumentInitializationNode extends StatementNode {

    private final int paramIndex;
    private final String identifier;
    private final PascalContext calleeContext;

    public SubroutineArgumentInitializationNode(int paramIndex, String identifier, PascalContext calleeContext) {
        this.paramIndex = paramIndex + 1;
        this.identifier = identifier;
        this.calleeContext = calleeContext;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        PascalRootNode rootNode = (PascalRootNode) this.calleeContext.getFunctionRegistry().lookup(identifier).getCallTarget().getRootNode();

        this.currentContext.registerSubroutineName(this.identifier);
        this.currentContext.setSubroutineRootNode(this.identifier, rootNode);
    }

}
