package fr.awildelephant.rdbms.data.value;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DomainValue {

    boolean isNull();

    int getInt();

    BigDecimal getBigDecimal();

    LocalDate getLocalDate();

    String getString();
}
