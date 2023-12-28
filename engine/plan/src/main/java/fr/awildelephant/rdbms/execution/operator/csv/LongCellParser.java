package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.LongValue.longValue;

public final class LongCellParser implements CellParser {

    @Override
    public DomainValue apply(String cell) {
        return longValue(Long.parseLong(cell));
    }
}
