package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.bool.BooleanOperation;
import fr.awildelephant.rdbms.evaluator.operation.date.DateOperation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalOperation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;

public final class ComparisonFactory {

    private ComparisonFactory() {

    }

    public static BooleanOperation equalComparison(final Operation left, final Operation right) {
        if (left instanceof IntegerOperation && right instanceof IntegerOperation) {
            return new IntegerIntegerEqualComparison(left, right);
        } else if (left instanceof DecimalOperation && right instanceof DecimalOperation) {
            return new DecimalDecimalEqualComparison(left, right);
        } else if (left instanceof TextOperation && right instanceof TextOperation) {
            return new TextTextEqualComparison((TextOperation) left, (TextOperation) right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + left.getClass() + " = " + right.getClass());
        }
    }

    public static BooleanOperation lessComparison(final Operation left, final Operation right) {
        if (left instanceof IntegerOperation && right instanceof IntegerOperation) {
            return new IntegerIntegerLessComparison(left, right);
        } else if (left instanceof DecimalOperation && right instanceof DecimalOperation) {
            return new DecimalDecimalLessComparison(left, right);
        } else if (left instanceof DateOperation && right instanceof DateOperation) {
            // TODO: se baser sur le type des fils plutôt que sur le domaine (?)
            return new DateDateLessComparison((DateOperation) left, (DateOperation) right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + left.getClass() + " < " + right.getClass());
        }

    }

    public static BooleanOperation lessOrEqualComparison(final Operation left, final Operation right) {
        if (left instanceof DateOperation && right instanceof DateOperation) {
            return new DateDateLessOrEqualComparison((DateOperation) left, (DateOperation) right);
        } else if (left instanceof IntegerOperation && right instanceof IntegerOperation) {
            return new IntegerIntegerLessOrEqualComparison(left, right);
        } else if (left instanceof DecimalOperation && right instanceof DecimalOperation) {
            return new DecimalLessOrEqualComparison(left, right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + left.getClass() + " <= " + right.getClass());
        }
    }
}
