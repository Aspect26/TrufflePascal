package cz.cuni.mff.d3s.trupple.parser;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.IdentifiersTable;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO: this class shall became unnecessary
class InitializationNodeGenerator {

    private final IdentifiersTable identifiersTable;
    private final VirtualFrame initializationFrame;

    InitializationNodeGenerator(IdentifiersTable identifiersTable, VirtualFrame initializationFrame) {
        this.identifiersTable = identifiersTable;
        this.initializationFrame = initializationFrame;
    }

    InitializationNodeGenerator(IdentifiersTable identifiersTable) {
        this(identifiersTable, null);
    }

    List<StatementNode> generate()  {
        List<StatementNode> initializationNodes = new ArrayList<>();

        for (Map.Entry<String, TypeDescriptor> entry : this.identifiersTable.getAllIdentifiers().entrySet()) {
            String identifier = entry.getKey();
            TypeDescriptor typeDescriptor = entry.getValue();

            StatementNode initializationNode = createInitializationNode(identifier, typeDescriptor);
            if (initializationNode != null) {
                initializationNodes.add(initializationNode);
            }
        }

        return initializationNodes;
    }

    private StatementNode createInitializationNode(String identifier, TypeDescriptor typeDescriptor) {
        Object defaultValue = typeDescriptor.getDefaultValue();
        if (defaultValue == null) {
            return null;
        }

        FrameSlot frameSlot = this.identifiersTable.getFrameSlot(identifier);
        return InitializationNodeFactory.create(frameSlot, typeDescriptor.getDefaultValue(), this.initializationFrame);
    }

}
