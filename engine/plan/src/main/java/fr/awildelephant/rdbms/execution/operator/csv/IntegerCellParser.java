package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public final class IntegerCellParser implements CellParser {

    @Override
    public DomainValue apply(String cell) {
        return integerValue(Integer.parseInt(cell));
    }
}
