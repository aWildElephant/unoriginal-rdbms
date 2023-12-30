package fr.awildelephant.rdbms.evaluator.operation.cast;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.DATE;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;

public final class CastOperationFactory {

    private CastOperationFactory() {

    }

    public static Operation build(final Operation input, final Domain target) {
        if (input instanceof final IntegerOperation integerOperation) {
            if (target == TEXT) {
                return new IntegerToTextCastOperation(integerOperation);
            }
        } else if (input instanceof final TextOperation textOperation) {
            if (target == DATE) {
                return new TextToDateCastOperation(textOperation);
            }
            if (target == INTEGER) {
                return new TextToIntegerCastOperation(textOperation);
            }
        }

        throw new UnsupportedOperationException("Unsupported cast from " + input.domain() + " to " + target);
    }
}
