package fr.awildelephant.rdbms.evaluator.operation.comparison;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.DATE;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;

public final class ComparisonFactory {

    private ComparisonFactory() {

    }

    public static Operation equalComparison(Operation left, Operation right) {
        final Domain leftDomain = left.domain();
        final Domain rightDomain = right.domain();

        if (leftDomain == INTEGER && rightDomain == INTEGER) {
            return new IntegerIntegerEqualComparison(left, right);
        } else if (leftDomain == TEXT && rightDomain == TEXT) {
            return new TextTextEqualComparison(left, right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + leftDomain + " = " + rightDomain);
        }
    }

    public static Operation lessOrEqualComparison(Operation left, Operation right) {
        final Domain leftDomain = left.domain();
        final Domain rightDomain = right.domain();

        if (leftDomain == DATE && rightDomain == DATE) {
            return new DateDateLessOrEqualComparison(left, right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + leftDomain + " <= " + rightDomain);
        }
    }
}
