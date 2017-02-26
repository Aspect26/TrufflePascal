package cz.cuni.mff.d3s.trupple.language.parser;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;

import java.util.ArrayList;
import java.util.List;

public class CaseStatementData {
    public ExpressionNode caseExpression;
    public final List<ExpressionNode> indexNodes = new ArrayList<ExpressionNode>();
    public final List<StatementNode> statementNodes = new ArrayList<StatementNode>();
    public StatementNode elseNode;
}