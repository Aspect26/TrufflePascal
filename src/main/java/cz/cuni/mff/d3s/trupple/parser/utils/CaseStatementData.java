package cz.cuni.mff.d3s.trupple.parser.utils;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure holding information about a case statement. It contains node of the control expression. nodes defining
 * the cases (condition and statement) and fallback node.
 */
public class CaseStatementData {
    public ExpressionNode caseExpression;
    public final List<ExpressionNode> indexNodes = new ArrayList<>();
    public final List<StatementNode> statementNodes = new ArrayList<>();
    public StatementNode elseNode;
}