package fr.awildelephant.rdbms.evaluator.operation.cast;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.date.TextToDateCastOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.DATE;

public final class CastOperationFactory {

    private CastOperationFactory() {

    }

    public static Operation build(Operation input, Domain target) {
        if (target != DATE || !(input instanceof TextOperation)) {
            // TODO: proper exception
            throw new IllegalStateException();
        }

        return new TextToDateCastOperation((TextOperation) input);
    }
}
