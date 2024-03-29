package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public interface IntegerOperation extends Operation, LongOperation, DecimalOperation {

    @Override
    default DomainValue evaluateAndWrap() {
        final Integer value = evaluateInteger();

        if (value == null) {
            return nullValue();
        }

        return integerValue(value);
    }

    @Override
    default Long evaluateLong() {
        final Integer value = evaluateInteger();
        if (value == null) {
            return null;
        }

        return value.longValue();
    }

    @Override
    default BigDecimal evaluateBigDecimal() {
        final Integer value = evaluateInteger();
        if (value == null) {
            return null;
        }

        return new BigDecimal(value);
    }

    Integer evaluateInteger();

    @Override
    default Domain domain() {
        return INTEGER;
    }
}
