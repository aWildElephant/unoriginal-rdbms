package fr.awildelephant.rdbms.features;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.function.Supplier;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public enum Checker {

    DATE {
        @Override
        void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException, ParseException {
            final Date actualDate = actual.getDate(columnPosition);
            final Supplier<String> messageSupplier = errorMessage(rowPosition, columnPosition, expected, actualDate);

            if ("null".equalsIgnoreCase(expected)) {
                assertTrue(actual.wasNull(), messageSupplier);
            } else {
                assertEquals(ISO8601.parse(expected), actualDate, messageSupplier);
            }
        }
    },
    DECIMAL {
        @Override
        void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException {
            final BigDecimal actualDecimal = actual.getBigDecimal(columnPosition);
            final Supplier<String> messageSupplier = errorMessage(rowPosition, columnPosition, expected, actualDecimal);

            if ("null".equalsIgnoreCase(expected)) {
                assertTrue(actual.wasNull(), messageSupplier);
            } else {
                assertEquals(new BigDecimal(expected), actualDecimal, messageSupplier);
            }
        }
    },
    INTEGER {
        @Override
        void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException {
            final int actualInt = actual.getInt(columnPosition);
            final Supplier<String> messageSupplier = errorMessage(rowPosition, columnPosition, expected, actualInt);

            if ("null".equalsIgnoreCase(expected)) {
                assertTrue(actual.wasNull(), messageSupplier);
            } else {
                assertEquals(parseInt(expected), actualInt, messageSupplier);
            }
        }
    },
    TEXT {
        @Override
        void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException {
            final String actualString = actual.getString(columnPosition);
            final Supplier<String> messageSupplier = errorMessage(rowPosition, columnPosition, expected, actualString);

            if (actual.wasNull()) {
                assertTrue("null".equalsIgnoreCase(expected), messageSupplier);
            } else {
                assertEquals(expected, actualString, messageSupplier);
            }
        }
    };

    private static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd");

    public static Checker checkerFor(String typeName) {
        return Checker.valueOf(typeName);
    }

    private static Supplier<String> errorMessage(int rowIndex, int columnIndex, String expected, Object actual) {
        return () -> "Row " + rowIndex + " column " + columnIndex + " : expected " + expected + " but got " + actual;
    }

    abstract void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws Exception;
}
