package cz.cuni.mff.d3s.trupple.language.parser;

import com.oracle.truffle.api.frame.FrameSlot;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.IdentifiersTable;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class InitializationNodeGenerator {

    private final IdentifiersTable identifiersTable;

    InitializationNodeGenerator(IdentifiersTable identifiersTable) {
        this.identifiersTable = identifiersTable;
    }

    List<StatementNode> generate()  {
        List<StatementNode> initializationNodes = new ArrayList<>();

        for (Map.Entry<String, TypeDescriptor> entry : this.identifiersTable.getAll().entrySet()) {
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
        return InitializationNodeFactory.create(frameSlot, typeDescriptor.getDefaultValue());
    }

}
