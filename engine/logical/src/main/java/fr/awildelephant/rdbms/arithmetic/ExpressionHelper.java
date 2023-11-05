package fr.awildelephant.rdbms.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.arithmetic.ConstantExpression.constantExpression;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;

public final class ExpressionHelper {

    private ExpressionHelper() {

    }

    public static ValueExpression alwaysTrue() {
        return constantExpression(trueValue(), Domain.BOOLEAN);
    }
}
