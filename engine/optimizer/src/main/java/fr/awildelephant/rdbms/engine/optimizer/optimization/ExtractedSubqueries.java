package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import java.util.List;

public class ExtractedSubqueries {

    private final ValueExpression expression;
    private final List<LogicalOperator> subqueries;

    public ExtractedSubqueries(ValueExpression expression, List<LogicalOperator> subqueries) {
        this.expression = expression;
        this.subqueries = subqueries;
    }

    public ValueExpression expression() {
        return expression;
    }

    public List<LogicalOperator> subqueries() {
        return subqueries;
    }
}
