package fr.awildelephant.rdbms.engine.data.value;

import java.math.BigDecimal;

public interface DomainValue {

    boolean isNull();

    int getInt();

    BigDecimal getBigDecimal();

    String getString();
}
