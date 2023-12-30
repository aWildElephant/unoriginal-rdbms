package fr.awildelephant.rdbms.evaluator.operation.interval;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import java.time.Period;

import static fr.awildelephant.rdbms.data.value.IntervalValue.intervalValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.INTERVAL;

public interface IntervalOperation extends Operation {

    @Override
    default DomainValue evaluateAndWrap() {
        final Period value = evaluate();

        if (value == null) {
            return nullValue();
        }

        return intervalValue(value);
    }

    @Override
    default Domain domain() {
        return INTERVAL;
    }

    Period evaluate();
}
