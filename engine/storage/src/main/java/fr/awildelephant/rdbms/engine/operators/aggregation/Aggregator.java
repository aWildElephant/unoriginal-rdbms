package fr.awildelephant.rdbms.engine.operators.aggregation;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface Aggregator {

    /**
     * @return true if the result is known and there is no need to continue accumulating
     */
    boolean accumulate(DomainValue value);

    DomainValue aggregate();
}
