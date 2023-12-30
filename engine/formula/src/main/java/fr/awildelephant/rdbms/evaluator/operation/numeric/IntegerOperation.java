package fr.awildelephant.rdbms.evaluator.operation.numeric;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public interface IntegerOperation extends Operation {

    @Override
    default DomainValue evaluateAndWrap() {
        final Integer value = evaluate();

        if (value == null) {
            return nullValue();
        }

        return integerValue(value);
    }

    @Override
    default Domain domain() {
        return INTEGER;
    }

    Integer evaluate();
}
