package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import java.util.List;

public final class MultiJoin {

    private final List<ValueExpression> expressions;
    private final List<LogicalOperator> operators;

    public MultiJoin(List<LogicalOperator> operators, List<ValueExpression> expressions) {
        this.expressions = expressions;
        this.operators = operators;
    }

    public List<ValueExpression> expressions() {
        return expressions;
    }

    public List<LogicalOperator> operators() {
        return operators;
    }
}
