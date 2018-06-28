package fr.awildelephant.rdbms.features;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

public enum Checker {

    DECIMAL {
        @Override
        void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException {
            final BigDecimal actualDecimal = actual.getBigDecimal(columnPosition);

            assertEquals(new BigDecimal(expected), actualDecimal, errorMessage(rowPosition, columnPosition, expected, actualDecimal));
        }
    },
    INTEGER {
        @Override
        void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException {
            final int actualInt = actual.getInt(columnPosition);

            assertEquals(parseInt(expected), actualInt, errorMessage(rowPosition, columnPosition, expected, actualInt));
        }
    },
    TEXT {
        @Override
        void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException {
            final String actualString = actual.getString(columnPosition);

            assertEquals(expected, actualString, errorMessage(rowPosition, columnPosition, expected, actualString));
        }
    };

    public static Checker checkerFor(String typeName) {
        return Checker.valueOf(typeName);
    }

    private static Supplier<String> errorMessage(int rowIndex, int columnIndex, String expected, Object actual) {
        return () -> "Row " + rowIndex + " column " + columnIndex + " : expected " + expected + " but got " + actual;
    }

    abstract void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException;
}
