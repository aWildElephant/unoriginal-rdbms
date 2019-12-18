package fr.awildelephant.rdbms.engine.optimizer.optimization;

import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.DefaultLopVisitor;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

// TODO: if we modify this class in a way that makes us decorrelate Q2, we'll need to reorder the join or we'll run out of heap space
public final class BreakdownFinder extends DefaultLopVisitor<Boolean> {

    private static final BreakdownFinder INSTANCE = new BreakdownFinder();

    public static boolean canAddBreakdownOver(LogicalOperator plan) {
        return Boolean.FALSE.equals(INSTANCE.apply(plan));
    }

    @Override
    public Boolean visit(AliasLop alias) {
        return apply(alias.input());
    }

    @Override
    public Boolean visit(BaseTableLop baseTable) {
        return FALSE;
    }

    @Override
    public Boolean visit(FilterLop filter) {
        return apply(filter.input());
    }

    @Override
    public Boolean visit(MapLop mapNode) {
        return apply(mapNode.input());
    }

    @Override
    public Boolean defaultVisit(LogicalOperator operator) {
        return TRUE;
    }
}
