package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.bool.BooleanOperation;
import fr.awildelephant.rdbms.evaluator.operation.date.DateOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.schema.Domain.DATE;
import static fr.awildelephant.rdbms.schema.Domain.DECIMAL;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;
import static fr.awildelephant.rdbms.schema.Domain.TEXT;

public final class ComparisonFactory {

    private ComparisonFactory() {

    }

    public static BooleanOperation equalComparison(Operation left, Operation right) {
        final Domain leftDomain = left.domain();
        final Domain rightDomain = right.domain();

        if (leftDomain == INTEGER && rightDomain == INTEGER) {
            return new IntegerIntegerEqualComparison(left, right);
        } else if (leftDomain.canBeUsedAs(DECIMAL) && rightDomain.canBeUsedAs(DECIMAL)) {
            return new DecimalDecimalEqualComparison(left, right);
        } else if (leftDomain == TEXT && rightDomain == TEXT) {
            return new TextTextEqualComparison((TextOperation) left, (TextOperation) right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + leftDomain + " = " + rightDomain);
        }
    }

    public static BooleanOperation lessComparison(Operation left, Operation right) {
        final Domain leftDomain = left.domain();
        final Domain rightDomain = right.domain();

        if (leftDomain == INTEGER && rightDomain == INTEGER) {
            return new IntegerIntegerLessComparison(left, right);
        } else if (leftDomain.canBeUsedAs(DECIMAL) && rightDomain.canBeUsedAs(DECIMAL)) {
            return new DecimalDecimalLessComparison(left, right);
        } else if (leftDomain == DATE && rightDomain == DATE) {
            // TODO: se baser sur le type des fils plutôt que sur le domaine (?)
            return new DateDateLessComparison((DateOperation) left, (DateOperation) right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + leftDomain + " < " + rightDomain);
        }

    }

    public static BooleanOperation lessOrEqualComparison(Operation left, Operation right) {
        final Domain leftDomain = left.domain();
        final Domain rightDomain = right.domain();

        if (leftDomain == DATE && rightDomain == DATE) {
            return new DateDateLessOrEqualComparison((DateOperation) left, (DateOperation) right);
        } else if (leftDomain == INTEGER && rightDomain == INTEGER) {
            return new IntegerIntegerLessOrEqualComparison(left, right);
        } else if (leftDomain.canBeUsedAs(DECIMAL) && rightDomain.canBeUsedAs(DECIMAL)) {
            return new DecimalLessOrEqualComparison(left, right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + leftDomain + " <= " + rightDomain);
        }
    }
}
