package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.data.value.LongValue.longValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.LONG;

public interface LongOperation extends Operation, DecimalOperation {

    @Override
    default DomainValue evaluateAndWrap() {
        final Long value = evaluateLong();
        if (value == null) {
            return nullValue();
        }

        return longValue(value);
    }

    @Override
    default BigDecimal evaluateBigDecimal() {
        final Long value = evaluateLong();
        if (value == null) {
            return null;
        }
        return new BigDecimal(value);
    }

    Long evaluateLong();

    @Override
    default Domain domain() {
        return LONG;
    }
}
