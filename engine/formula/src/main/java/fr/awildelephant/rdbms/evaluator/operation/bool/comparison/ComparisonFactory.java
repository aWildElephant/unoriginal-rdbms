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
            return new IntegerIntegerEqualComparison((IntegerOperation) left, (IntegerOperation) right);
        } else if (left instanceof DecimalOperation && right instanceof DecimalOperation) {
            return new DecimalDecimalEqualComparison((DecimalOperation) left, (DecimalOperation) right);
        } else if (left instanceof TextOperation && right instanceof TextOperation) {
            return new TextTextEqualComparison((TextOperation) left, (TextOperation) right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + left.getClass() + " = " + right.getClass());
        }
    }

    public static BooleanOperation lessComparison(final Operation left, final Operation right) {
        if (left instanceof IntegerOperation && right instanceof IntegerOperation) {
            return new IntegerIntegerLessComparison((IntegerOperation) left, (IntegerOperation) right);
        } else if (left instanceof DecimalOperation && right instanceof DecimalOperation) {
            return new DecimalDecimalLessComparison((DecimalOperation) left, (DecimalOperation) right);
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
            return new IntegerIntegerLessOrEqualComparison((IntegerOperation) left, (IntegerOperation) right);
        } else if (left instanceof DecimalOperation && right instanceof DecimalOperation) {
            return new DecimalLessOrEqualComparison((DecimalOperation) left, (DecimalOperation) right);
        } else {
            throw new UnsupportedOperationException("Unsupported comparison: " + left.getClass() + " <= " + right.getClass());
        }
    }
}
