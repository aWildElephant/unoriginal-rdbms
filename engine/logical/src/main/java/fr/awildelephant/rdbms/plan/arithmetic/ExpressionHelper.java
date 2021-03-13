package fr.awildelephant.rdbms.plan.arithmetic;

import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.plan.arithmetic.ConstantExpression.constantExpression;

public final class ExpressionHelper {

    private ExpressionHelper() {

    }

    public static ValueExpression alwaysTrue() {
        return constantExpression(trueValue(), Domain.BOOLEAN);
    }
}
