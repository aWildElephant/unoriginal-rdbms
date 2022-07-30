package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.execution.alias.ReversibleAlias;
import fr.awildelephant.rdbms.execution.arithmetic.DefaultValueExpressionVisitor;
import fr.awildelephant.rdbms.execution.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.execution.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.execution.arithmetic.Variable;

import static fr.awildelephant.rdbms.execution.arithmetic.OuterQueryVariable.outerQueryVariable;
import static fr.awildelephant.rdbms.execution.arithmetic.Variable.variable;

public final class ExpressionUnaliaser extends DefaultValueExpressionVisitor<ValueExpression> {

    private final ReversibleAlias alias;

    ExpressionUnaliaser(ReversibleAlias alias) {
        this.alias = alias;
    }

    @Override
    public ValueExpression visit(OuterQueryVariable outerQueryVariable) {
        return outerQueryVariable(alias.unalias(outerQueryVariable.reference()), outerQueryVariable.domain());
    }

    @Override
    public ValueExpression visit(Variable variable) {
        return variable(alias.unalias(variable.reference()), variable.domain());
    }

    @Override
    protected ValueExpression defaultVisit(ValueExpression expression) {
        return expression.transformInputs(this);
    }
}
