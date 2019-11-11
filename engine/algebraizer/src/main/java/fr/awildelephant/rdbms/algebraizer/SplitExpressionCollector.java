package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;

import java.util.ArrayList;
import java.util.List;

final class SplitExpressionCollector {

    private final List<AST> mapsBelowAggregates = new ArrayList<>();
    private final List<Aggregate> aggregates = new ArrayList<>();
    private final List<AST> mapsAboveAggregates = new ArrayList<>();

    void addMapBelowAggregates(AST map) {
        mapsBelowAggregates.add(map);
    }

    void addAggregate(Aggregate aggregate) {
        aggregates.add(aggregate);
    }

    void addMapAboveAggregates(AST map) {
        mapsAboveAggregates.add(map);
    }

    List<AST> mapsBelowAggregates() {
        return mapsBelowAggregates;
    }

    List<Aggregate> aggregates() {
        return aggregates;
    }

    List<AST> mapsAboveAggregates() {
        return mapsAboveAggregates;
    }
}
