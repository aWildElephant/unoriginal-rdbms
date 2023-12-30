package fr.awildelephant.rdbms.evaluator.operation.nil;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.ConstantOperation;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.evaluator.operation.bool.BooleanOperation;
import fr.awildelephant.rdbms.evaluator.operation.date.DateOperation;
import fr.awildelephant.rdbms.evaluator.operation.interval.IntervalOperation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.DecimalOperation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.IntegerOperation;
import fr.awildelephant.rdbms.evaluator.operation.numeric.LongOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.time.LocalDate;
import java.time.Period;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.NULL;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

public final class NullConstant extends ConstantOperation implements Operation, BooleanOperation, DateOperation, DecimalOperation, IntegerOperation, IntervalOperation, LongOperation, TextOperation {

    @Override
    public DomainValue evaluateAndWrap() {
        return nullValue();
    }

    @Override
    public Domain domain() {
        return NULL;
    }

    @Override
    public ThreeValuedLogic evaluateBoolean() {
        return UNKNOWN;
    }

    @Override
    public LocalDate evaluateLocalDate() {
        return null;
    }

    @Override
    public Period evaluatePeriod() {
        return null;
    }

    @Override
    public Integer evaluateInteger() {
        return null;
    }

    @Override
    public String evaluateString() {
        return null;
    }
}
