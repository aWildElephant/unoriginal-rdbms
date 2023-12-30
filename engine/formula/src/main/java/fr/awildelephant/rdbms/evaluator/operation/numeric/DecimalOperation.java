package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;

public interface DecimalOperation extends Operation {

    @Override
    default DomainValue evaluateAndWrap() {
        final BigDecimal value = evaluateBigDecimal();
        if (value == null) {
            return nullValue();
        }

        return decimalValue(value);
    }

    BigDecimal evaluateBigDecimal();

    @Override
    default Domain domain() {
        return DECIMAL;
    }
}
