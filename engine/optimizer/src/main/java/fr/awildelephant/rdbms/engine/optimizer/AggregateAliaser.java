package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.operator.logical.aggregation.Aggregate;
import fr.awildelephant.rdbms.operator.logical.aggregation.AnyAggregate;
import fr.awildelephant.rdbms.operator.logical.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.operator.logical.aggregation.CountAggregate;
import fr.awildelephant.rdbms.operator.logical.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.operator.logical.aggregation.MaxAggregate;
import fr.awildelephant.rdbms.operator.logical.aggregation.MinAggregate;
import fr.awildelephant.rdbms.operator.logical.aggregation.SumAggregate;
import fr.awildelephant.rdbms.operator.logical.alias.Alias;
import fr.awildelephant.rdbms.operator.logical.alias.ExactMatchAlias;

public final class AggregateAliaser {

    private final Alias inputAliaser;
    private final Alias outputAliaser;

    public AggregateAliaser(Alias inputAliaser, ExactMatchAlias outputAliaser) {
        this.inputAliaser = inputAliaser;
        this.outputAliaser = outputAliaser;
    }

    public Aggregate alias(Aggregate aggregate) {
        return switch (aggregate) {
            case AnyAggregate anyAggregate -> doAlias(anyAggregate);
            case AvgAggregate avgAggregate -> doAlias(avgAggregate);
            case CountAggregate countAggregate -> doAlias(countAggregate);
            case CountStarAggregate countStarAggregate -> doAlias(countStarAggregate);
            case MaxAggregate maxAggregate -> doAlias(maxAggregate);
            case MinAggregate minAggregate -> doAlias(minAggregate);
            case SumAggregate sumAggregate -> doAlias(sumAggregate);
            case null -> throw new IllegalStateException();
            default ->
                    throw new UnsupportedOperationException("Aliasing input of " + aggregate.getClass().getSimpleName() + " not supported");
        };
    }

    private AnyAggregate doAlias(AnyAggregate aggregate) {
        return new AnyAggregate(inputAliaser.alias(aggregate.input()), outputAliaser.alias(aggregate.outputColumn()));
    }

    private Aggregate doAlias(AvgAggregate aggregate) {
        return new AvgAggregate(inputAliaser.alias(aggregate.input()), outputAliaser.alias(aggregate.outputColumn()));
    }

    private CountAggregate doAlias(CountAggregate aggregate) {
        return new CountAggregate(inputAliaser.alias(aggregate.input()),
                                  outputAliaser.alias(aggregate.outputColumn()),
                                  aggregate.distinct());
    }

    private CountStarAggregate doAlias(CountStarAggregate aggregate) {
        return new CountStarAggregate(outputAliaser.alias(aggregate.outputColumn()));
    }

    private MaxAggregate doAlias(MaxAggregate aggregate) {
        return new MaxAggregate(inputAliaser.alias(aggregate.input()), outputAliaser.alias(aggregate.outputColumn()));
    }

    private MinAggregate doAlias(MinAggregate aggregate) {
        return new MinAggregate(inputAliaser.alias(aggregate.input()), outputAliaser.alias(aggregate.outputColumn()));
    }

    private SumAggregate doAlias(SumAggregate aggregate) {
        return new SumAggregate(inputAliaser.alias(aggregate.input()), outputAliaser.alias(aggregate.outputColumn()));
    }
}
