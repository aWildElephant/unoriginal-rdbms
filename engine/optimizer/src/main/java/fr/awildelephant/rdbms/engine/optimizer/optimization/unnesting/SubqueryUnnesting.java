package fr.awildelephant.rdbms.engine.optimizer.optimization.unnesting;

import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.DependentJoinLop;

public class SubqueryUnnesting extends DefaultLopVisitor<LogicalOperator> {

    @Override
    public LogicalOperator visit(DependentJoinLop dependentJoin) {
        final DependentJoinLop transformedDependentJoin = new DependentJoinLop(apply(dependentJoin.left()),
                                                                               apply(dependentJoin.right()),
                                                                               dependentJoin.predicate());

        return new NeumannKemperSubqueryDecorrelator().decorrelate(transformedDependentJoin);
    }

    @Override
    public LogicalOperator defaultVisit(LogicalOperator operator) {
        return operator.transformInputs(this);
    }
}