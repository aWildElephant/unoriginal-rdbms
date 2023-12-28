package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;

public final class BooleanCellParser implements CellParser {

    @Override
    public DomainValue apply(String cell) {
        if ("true".equalsIgnoreCase(cell)) {
            return trueValue();
        }

        if ("false".equalsIgnoreCase(cell)) {
            return falseValue();
        }

        throw new UnexpectedCSVCellValueException(cell, Domain.BOOLEAN);
    }
}
