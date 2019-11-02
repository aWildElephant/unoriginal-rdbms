package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

public class CaseWhenOperation implements Operation {

    private final Operation condition;
    private final Operation thenExpression;
    private final Operation elseExpression;
    private final Domain outputDomain;

    private CaseWhenOperation(Operation condition, Operation thenExpression, Operation elseExpression, Domain outputDomain) {
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
        this.outputDomain = outputDomain;
    }

    public static CaseWhenOperation caseWhenOperation(Operation condition, Operation thenExpression, Operation elseExpression, Domain outputDomain) {
        return new CaseWhenOperation(condition, thenExpression, elseExpression, outputDomain);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue check = condition.evaluate();

        if (check.isNull() || !check.getBool()) {
            return elseExpression.evaluate();
        } else {
            return thenExpression.evaluate();
        }
    }

    @Override
    public Domain domain() {
        return outputDomain;
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
