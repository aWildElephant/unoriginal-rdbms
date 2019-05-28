package fr.awildelephant.rdbms.jdbc.abstraction;

import java.math.BigDecimal;
import java.sql.Date;

public interface Value {

    boolean isNull();

    boolean getBoolean();

    BigDecimal getBigDecimal();

    Date getDate();

    int getInt();

    String getString();
}
