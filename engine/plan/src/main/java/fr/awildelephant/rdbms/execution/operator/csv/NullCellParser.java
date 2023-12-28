package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;
import org.apache.commons.lang3.StringUtils;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;

public final class NullCellParser implements CellParser {

    @Override
    public DomainValue apply(String cell) {
        if (StringUtils.isBlank(cell) || "null".equalsIgnoreCase(cell)) {
            return nullValue();
        }

        throw new UnexpectedCSVCellValueException(cell, Domain.NULL);
    }
}
