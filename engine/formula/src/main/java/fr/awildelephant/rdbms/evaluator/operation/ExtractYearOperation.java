package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.date.DateOperation;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.time.LocalDate;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public final class ExtractYearOperation extends UnaryNode<Operation, DateOperation> implements Operation {

    public ExtractYearOperation(final DateOperation input) {
        super(input);
    }

    @Override
    public DomainValue evaluateAndWrap() {
        final LocalDate date = child().evaluate();

        if (date == null) {
            return nullValue();
        }

        return integerValue(date.getYear());
    }

    @Override
    public Domain domain() {
        return INTEGER;
    }

    @Override
    public boolean isConstant() {
        return child().isConstant();
    }
}
