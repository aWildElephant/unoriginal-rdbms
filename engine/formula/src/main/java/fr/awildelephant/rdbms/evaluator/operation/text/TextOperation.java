package fr.awildelephant.rdbms.evaluator.operation.text;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TextValue.textValue;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;

public interface TextOperation extends Operation {

    @Override
    default DomainValue evaluateAndWrap() {
        final String value = evaluateString();

        if (value == null) {
            return nullValue();
        }

        return textValue(value);
    }

    @Override
    default Domain domain() {
        return TEXT;
    }

    String evaluateString();
}
