package fr.awildelephant.rdbms.explain;

import fr.awildelephant.rdbms.plan.*;
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
        // TODO

        return null;
    }

    @Override
    public Void visit(FilterLop filter) {
        // TODO

        return null;
    }

    @Override
    public Void visit(InnerJoinLop innerJoinLop) {
        // TODO

        return null;
    }

    @Override
    public Void visit(LeftJoinLop leftJoinLop) {
        // TODO

        return null;
    }

    @Override
    public Void visit(LimitLop limitLop) {
        // TODO

        return null;
    }

    @Override
    public Void visit(MapLop mapNode) {
        // TODO

        return null;
    }

    @Override
    public Void visit(ProjectionLop projectionNode) {
        // TODO

        return null;
    }

    @Override
    public Void visit(ScalarSubqueryLop scalarSubquery) {
        // TODO

        return null;
    }

    @Override
    public Void visit(SemiJoinLop semiJoin) {
        // TODO

        return null;
    }

    @Override
    public Void visit(SortLop sortLop) {
        // TODO

        return null;
    }

    @Override
    public Void visit(DependentJoinLop subqueryExecutionLop) {
        // TODO

        return null;
    }

    @Override
    public Void visit(TableConstructorLop tableConstructor) {
        jsonBuilder.startObject();
        jsonBuilder.field("type", "constructor");
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
        // TODO

        return null;
    }

    @Override
    public String toString() {
        return jsonBuilder.toString();
    }
}
