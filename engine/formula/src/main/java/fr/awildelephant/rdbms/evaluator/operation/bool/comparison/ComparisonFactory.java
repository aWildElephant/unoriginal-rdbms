package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.bool.BooleanOperation;
import fr.awildelephant.rdbms.evaluator.operation.date.DateOperation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalOperation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;

// FIXME: not all combinations are implemented
public final class ComparisonFactory {

    private ComparisonFactory() {

    }

    public static BooleanOperation equalComparison(final Operation left, final Operation right) {
        return switch (left) {
            case IntegerOperation integerLeft when right instanceof final IntegerOperation integerRight ->
                    new IntegerIntegerEqualComparison(integerLeft, integerRight);
            case DecimalOperation decimalLeft when right instanceof final DecimalOperation decimalRight ->
                    new DecimalDecimalEqualComparison(decimalLeft, decimalRight);
            case TextOperation textLeft when right instanceof final TextOperation textRight ->
                    new TextTextEqualComparison(textLeft, textRight);
            case null -> throw new IllegalStateException();
            default ->
                    throw new UnsupportedOperationException("Unsupported comparison: " + left.getClass() + " = " + right.getClass());
        };
    }

    public static BooleanOperation lessComparison(final Operation left, final Operation right) {
        return switch (left) {
            case IntegerOperation integerLeft when right instanceof final IntegerOperation integerRight ->
                    new IntegerIntegerLessComparison(integerLeft, integerRight);
            case DecimalOperation decimalLeft when right instanceof final DecimalOperation decimalRight ->
                    new DecimalDecimalLessComparison(decimalLeft, decimalRight);
            case DateOperation dateLeft when right instanceof final DateOperation dateRight ->
                // TODO: se baser sur le type des fils plutôt que sur le domaine (?)
                    new DateDateLessComparison(dateLeft, dateRight);
            case null -> throw new IllegalStateException();
            default ->
                    throw new UnsupportedOperationException("Unsupported comparison: " + left.getClass() + " < " + right.getClass());
        };

    }

    public static BooleanOperation lessOrEqualComparison(final Operation left, final Operation right) {
        return switch (left) {
            case DateOperation dateLeft when right instanceof final DateOperation dateRight ->
                    new DateDateLessOrEqualComparison(dateLeft, dateRight);
            case IntegerOperation integerLeft when right instanceof final IntegerOperation integerRight ->
                    new IntegerIntegerLessOrEqualComparison(integerLeft, integerRight);
            case DecimalOperation decimalLeft when right instanceof final DecimalOperation decimalRight ->
                    new DecimalLessOrEqualComparison(decimalLeft, decimalRight);
            case null -> throw new IllegalStateException();
            default ->
                    throw new UnsupportedOperationException("Unsupported comparison: " + left.getClass() + " <= " + right.getClass());
        };
    }
}
