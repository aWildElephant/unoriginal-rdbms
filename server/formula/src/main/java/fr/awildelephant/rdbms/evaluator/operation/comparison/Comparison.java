package fr.awildelephant.rdbms.evaluator.operation.comparison;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public abstract class Comparison implements Operation {

    @Override
    public Domain domain() {
        return BOOLEAN;
    }
}
