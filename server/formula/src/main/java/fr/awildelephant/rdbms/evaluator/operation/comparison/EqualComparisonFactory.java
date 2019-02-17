package fr.awildelephant.rdbms.evaluator.operation.comparison;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.INTEGER;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;

public final class EqualComparisonFactory {

    private EqualComparisonFactory() {

    }

    public static Operation equalComparison(Operation left, Operation right) {
        final Domain leftInputDomain = left.domain();
        final Domain rightInputDomain = right.domain();

        if (leftInputDomain == INTEGER && rightInputDomain == INTEGER) {
            return new IntegerIntegerEqualComparison(left, right);
        } else if (leftInputDomain == TEXT && rightInputDomain == TEXT) {
            return new TextTextEqualComparison(left, right);
        } else {
            throw new UnsupportedOperationException(
                    "Unsupported equal comparison between " + leftInputDomain + " and " + rightInputDomain);
        }
    }
}
