package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;

import java.math.BigDecimal;

// TODO: we can probably put more stuff in this and move it around
public final class DomainValueUtils {

    private DomainValueUtils() {

    }

    public static Integer extractInteger(final Operation operation) {
        final DomainValue domainValue = operation.evaluateAndWrap();

        if (domainValue.isNull()) {
            return null;
        }

        return domainValue.getInt();
    }

    public static BigDecimal extractBigDecimal(final Operation operation) {
        final DomainValue domainValue = operation.evaluateAndWrap();

        if (domainValue.isNull()) {
            return null;
        }

        return domainValue.getBigDecimal();
    }
}
