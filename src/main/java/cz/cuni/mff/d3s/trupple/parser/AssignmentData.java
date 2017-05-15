package cz.cuni.mff.d3s.trupple.parser;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;

import java.util.Collections;
import java.util.List;

// TODO: rozdelit na viacero tried ...
public class AssignmentData {

    public enum Type {
        Simple, Array, Dereference, Record
    }

    public Type type;

    ExpressionNode targetNode;

    private ExpressionNode nextTargetNode;

    Token targetIdentifier;

    ExpressionNode arrayIndexNode;

    public void setSimple(NodeFactory factory, Token identifierToken) {
        this.targetIdentifier = identifierToken;
        this.type = AssignmentData.Type.Simple;

        this.nextTargetNode = factory.createReadVariableNode(identifierToken);
    }

    public void setArray(NodeFactory factory, List<ExpressionNode> indexNodes) {
        this.targetNode = this.nextTargetNode;
        this.type = Type.Array;
        this.arrayIndexNode = indexNodes.get(indexNodes.size() - 1);

        if (indexNodes.size() > 1) {
            this.targetNode = factory.createReadFromArrayNode(this.targetNode, indexNodes.subList(0, indexNodes.size() - 1));
        }
        this.nextTargetNode = factory.createReadFromArrayNode(this.targetNode, Collections.singletonList(this.arrayIndexNode));
    }

    public void setRecord(NodeFactory factory, Token identifierToken) {
        this.targetNode = this.nextTargetNode;
        this.type = Type.Record;
        this.targetIdentifier = identifierToken;

        this.nextTargetNode = factory.createReadFromRecordNode(this.targetNode, identifierToken);
    }

    public void setDereference(NodeFactory factory) {
        this.targetNode = this.nextTargetNode;
        this.type = AssignmentData.Type.Dereference;
        this.nextTargetNode = factory.createReadDereferenceNode(this.targetNode);
    }

}
