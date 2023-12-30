package fr.awildelephant.rdbms.evaluator.operation.date;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import java.time.LocalDate;

import static fr.awildelephant.rdbms.data.value.DateValue.dateValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.DATE;

public interface DateOperation extends Operation {

    @Override
    default DomainValue evaluateAndWrap() {
        final LocalDate value = evaluate();

        if (value == null) {
            return nullValue();
        }

        return dateValue(value);
    }

    @Override
    default Domain domain() {
        return DATE;
    }

    LocalDate evaluate();
}
