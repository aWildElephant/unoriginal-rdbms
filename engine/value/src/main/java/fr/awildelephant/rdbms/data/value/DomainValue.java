package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public interface DomainValue {

    boolean isNull();

    boolean getBool();

    int getInt();

    long getLong();

    BigDecimal getBigDecimal();

    LocalDate getLocalDate();

    Period getPeriod();

    String getString();
}
