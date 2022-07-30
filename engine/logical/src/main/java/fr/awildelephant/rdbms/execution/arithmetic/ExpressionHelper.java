package fr.awildelephant.rdbms.execution.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.execution.arithmetic.ConstantExpression.constantExpression;

public final class ExpressionHelper {

    private ExpressionHelper() {

    }

    public static ValueExpression alwaysTrue() {
        return constantExpression(trueValue(), Domain.BOOLEAN);
    }
}
