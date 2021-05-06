package fr.awildelephant.rdbms.server.explain;

import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DependentJoinLop;
import fr.awildelephant.rdbms.plan.DependentSemiJoinLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LeftJoinLop;
import fr.awildelephant.rdbms.plan.LimitLop;
import fr.awildelephant.rdbms.plan.LopVisitor;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.ScalarSubqueryLop;
import fr.awildelephant.rdbms.plan.SemiJoinLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;

import java.util.List;

public final class PlanJsonBuilder implements LopVisitor<Void> {

    private final JsonBuilder jsonBuilder = new JsonBuilder();
    private final ExpressionJsonBuilder expressionJsonBuilder = new ExpressionJsonBuilder(jsonBuilder);

    @Override
    public Void visit(AggregationLop aggregationNode) {
        return null;
    }

    @Override
    public Void visit(AliasLop alias) {
        return null;
    }

    @Override
    public Void visit(BaseTableLop baseTable) {
        return null;
    }

    @Override
    public Void visit(CartesianProductLop cartesianProductNode) {
        return null;
    }

    @Override
    public Void visit(DistinctLop distinctNode) {
        return null;
    }

    @Override
    public Void visit(FilterLop filter) {
        return null;
    }

    @Override
    public Void visit(InnerJoinLop innerJoinLop) {
        return null;
    }

    @Override
    public Void visit(LeftJoinLop leftJoinLop) {
        return null;
    }

    @Override
    public Void visit(LimitLop limitLop) {
        return null;
    }

    @Override
    public Void visit(MapLop mapNode) {
        return null;
    }

    @Override
    public Void visit(ProjectionLop projectionNode) {
        return null;
    }

    @Override
    public Void visit(ScalarSubqueryLop scalarSubquery) {
        return null;
    }

    @Override
    public Void visit(SemiJoinLop semiJoin) {
        return null;
    }

    @Override
    public Void visit(SortLop sortLop) {
        return null;
    }

    @Override
    public Void visit(DependentJoinLop subqueryExecutionLop) {
        return null;
    }

    @Override
    public Void visit(TableConstructorLop tableConstructor) {
        jsonBuilder.startObject()
                .field("type", "constructor");
        jsonBuilder.nextField();
        jsonBuilder.field("matrix");
        jsonBuilder.startArray();
        boolean firstRow = true;
        for (List<ValueExpression> row : tableConstructor.matrix()) {
            if (firstRow) {
                firstRow = false;
            } else {
                jsonBuilder.nextArrayElement();
            }

            boolean firstElement = true;
            jsonBuilder.startArray();
            for (ValueExpression value : row) {
                if (firstElement) {
                    firstElement = false;
                } else {
                    jsonBuilder.nextArrayElement();
                }
                expressionJsonBuilder.apply(value);
            }
            jsonBuilder.endArray();
        }
        jsonBuilder.endArray();
        jsonBuilder.endObject();

        return null;
    }

    @Override
    public Void visit(DependentSemiJoinLop dependentSemiJoinLop) {
        return null;
    }

    @Override
    public String toString() {
        return jsonBuilder.toString();
    }
}
