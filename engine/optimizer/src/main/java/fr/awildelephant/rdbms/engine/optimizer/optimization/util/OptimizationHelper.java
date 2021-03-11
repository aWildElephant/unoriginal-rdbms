package fr.awildelephant.rdbms.engine.optimizer.optimization.util;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.plan.arithmetic.ConstantExpression.constantExpression;

public final class OptimizationHelper {

    private OptimizationHelper() {

    }

    public static ValueExpression alwaysTrue() {
        return constantExpression(trueValue(), Domain.BOOLEAN);
    }
}
