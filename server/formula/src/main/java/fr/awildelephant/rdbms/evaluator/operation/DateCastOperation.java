package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import java.time.LocalDate;

import static fr.awildelephant.rdbms.data.value.DateValue.dateValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.DATE;

public class DateCastOperation implements Operation {

    private final Operation input;

    private DateCastOperation(Operation input) {
        this.input = input;
    }

    public static DateCastOperation dateCastOperation(Operation input) {
        return new DateCastOperation(input);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue inputValue = input.evaluate();

        if (inputValue.isNull()) {
            return nullValue();
        }

        return dateValue(LocalDate.parse(inputValue.getString()));
    }

    @Override
    public Domain domain() {
        return DATE;
    }

    @Override
    public boolean isConstant() {
        return input.isConstant();
    }
}
