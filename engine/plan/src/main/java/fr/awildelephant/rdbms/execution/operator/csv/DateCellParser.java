package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static fr.awildelephant.rdbms.data.value.DateValue.dateValue;

public final class DateCellParser implements CellParser {

    @Override
    public DomainValue apply(String cell) {
        try {
            return dateValue(LocalDate.parse(cell));
        } catch (DateTimeParseException e) {
            throw new UnexpectedCSVCellValueException(cell, Domain.DATE, e);
        }
    }
}
