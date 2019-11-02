package fr.awildelephant.rdbms.evaluator.operation.comparison;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.DATE;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
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

    public static Operation lessComparison(Operation left, Operation right) {
        final Domain leftDomain = left.domain();
        final Domain rightDomain = right.domain();

        if (leftDomain == INTEGER && rightDomain == INTEGER) {
            return new IntegerIntegerLessComparison(left, right);
        } else if (leftDomain == DECIMAL && rightDomain == DECIMAL) {
            return new DecimalDecimalLessComparison(left, right);
        } else if (leftDomain == DATE && rightDomain == DATE) {
            return new DateDateLessComparison(left, right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + leftDomain + " < " + rightDomain);
        }

    }

    public static Operation lessOrEqualComparison(Operation left, Operation right) {
        final Domain leftDomain = left.domain();
        final Domain rightDomain = right.domain();

        if (leftDomain == DATE && rightDomain == DATE) {
            return new DateDateLessOrEqualComparison(left, right);
        } else if (leftDomain == INTEGER && rightDomain == INTEGER) {
            return new IntegerIntegerLessOrEqualComparison(left, right);
        } else if (leftDomain.canBeUsedAs(DECIMAL) && rightDomain.canBeUsedAs(DECIMAL)) {
            return new DecimalLessOrEqualComparison(left, right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + leftDomain + " <= " + rightDomain);
        }
    }
}
