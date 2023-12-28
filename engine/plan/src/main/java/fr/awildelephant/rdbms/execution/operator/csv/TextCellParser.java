package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.data.value.DomainValue;

import static fr.awildelephant.rdbms.data.value.TextValue.textValue;

public final class TextCellParser implements CellParser {

    @Override
    public DomainValue apply(String cell) {
        return textValue(cell);
    }
}
