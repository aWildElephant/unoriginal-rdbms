package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public interface DomainValue {

    default boolean isNull() {
        return false;
    }

    default boolean getBool() {
        throw new UnsupportedOperationException();
    }

    default int getInt() {
        throw new UnsupportedOperationException();
    }

    default long getLong() {
        throw new UnsupportedOperationException();
    }

    default BigDecimal getBigDecimal() {
        throw new UnsupportedOperationException();
    }

    default LocalDate getLocalDate() {
        throw new UnsupportedOperationException();
    }

    default Period getPeriod() {
        throw new UnsupportedOperationException();
    }

    default String getString() {
        throw new UnsupportedOperationException();
    }
}
