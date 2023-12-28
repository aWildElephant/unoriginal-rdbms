package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.math.BigDecimal;

import static fr.awildelephant.rdbms.data.value.DecimalValue.decimalValue;

public final class DecimalCellParser implements CellParser {

    @Override
    public DomainValue apply(String cell) {
        return decimalValue(new BigDecimal(cell));
    }
}
