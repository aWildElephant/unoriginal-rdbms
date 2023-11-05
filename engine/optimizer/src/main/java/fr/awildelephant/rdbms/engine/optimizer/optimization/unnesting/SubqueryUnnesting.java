package fr.awildelephant.rdbms.engine.optimizer.optimization.unnesting;

import fr.awildelephant.rdbms.function.VariableCollector;
import fr.awildelephant.rdbms.operator.logical.DefaultLopVisitor;
import fr.awildelephant.rdbms.operator.logical.DependentJoinLop;
import fr.awildelephant.rdbms.operator.logical.DependentSemiJoinLop;
import fr.awildelephant.rdbms.operator.logical.LogicalOperator;

public final class SubqueryUnnesting extends DefaultLopVisitor<LogicalOperator> {

    private final VariableCollector variableCollector;

    public SubqueryUnnesting(VariableCollector variableCollector) {
        this.variableCollector = variableCollector;
    }

    @Override
    public LogicalOperator visit(DependentJoinLop dependentJoin) {
        final DependentJoinLop transformedDependentJoin = new DependentJoinLop(apply(dependentJoin.left()),
                apply(dependentJoin.right()),
                dependentJoin.predicate());

        return new NeumannKemperSubqueryDecorrelator(variableCollector).decorrelate(transformedDependentJoin);
    }

    @Override
    public LogicalOperator visit(DependentSemiJoinLop dependentSemiJoin) {
        final DependentSemiJoinLop transformedDependentSemiJoin = new DependentSemiJoinLop(
                apply(dependentSemiJoin.left()),
                apply(dependentSemiJoin.right()),
                dependentSemiJoin.predicate(),
                dependentSemiJoin.outputColumnName()
        );

        return new NeumannKemperSubqueryDecorrelator(variableCollector).decorrelate(transformedDependentSemiJoin);
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }
}
