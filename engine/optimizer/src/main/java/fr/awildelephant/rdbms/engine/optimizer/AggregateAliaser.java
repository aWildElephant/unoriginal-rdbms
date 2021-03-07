package fr.awildelephant.rdbms.engine.optimizer;

import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.aggregation.AnyAggregate;
import fr.awildelephant.rdbms.plan.aggregation.AvgAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountAggregate;
import fr.awildelephant.rdbms.plan.aggregation.CountStarAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MaxAggregate;
import fr.awildelephant.rdbms.plan.aggregation.MinAggregate;
import fr.awildelephant.rdbms.plan.aggregation.SumAggregate;
import fr.awildelephant.rdbms.plan.alias.Alias;
import fr.awildelephant.rdbms.plan.alias.ExactMatchAlias;

public final class AggregateAliaser {

    private final Alias inputAliaser;
    private final Alias outputAliaser;

    public AggregateAliaser(Alias inputAliaser, ExactMatchAlias outputAliaser) {
        this.inputAliaser = inputAliaser;
        this.outputAliaser = outputAliaser;
    }

    public Aggregate alias(Aggregate aggregate) {
        if (aggregate instanceof AnyAggregate) {
            return doAlias((AnyAggregate) aggregate);
        } else if (aggregate instanceof AvgAggregate) {
            return doAlias((AvgAggregate) aggregate);
        } else if (aggregate instanceof CountAggregate) {
            return doAlias((CountAggregate) aggregate);
        } else if (aggregate instanceof CountStarAggregate) {
            return doAlias((CountStarAggregate) aggregate);
        } else if (aggregate instanceof MaxAggregate) {
            return doAlias((MaxAggregate) aggregate);
        } else if (aggregate instanceof MinAggregate) {
            return doAlias((MinAggregate) aggregate);
        } else if (aggregate instanceof SumAggregate) {
            return doAlias((SumAggregate) aggregate);
        } else {
            throw new UnsupportedOperationException("Aliasing input of " + aggregate.getClass().getSimpleName() + " not supported");
        }
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
