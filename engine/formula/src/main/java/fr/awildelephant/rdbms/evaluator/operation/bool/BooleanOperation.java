package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public interface BooleanOperation extends Operation {

    @Override
    default DomainValue evaluateAndWrap() {
        final ThreeValuedLogic value = evaluate();

        return switch (value) {
            case UNKNOWN -> nullValue();
            case TRUE -> trueValue();
            case FALSE -> falseValue();
        };
    }

    @Override
    default Domain domain() {
        return BOOLEAN;
    }

    ThreeValuedLogic evaluate();
}
