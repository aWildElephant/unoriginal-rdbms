package fr.awildelephant.rdbms.test.commons;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.function.Supplier;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO: we try to get a value before checking DomainValue#wasNull: how bad will the error message be ?
public enum Checker {

    BOOLEAN {
        @Override
        public void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws Exception {
            final boolean actualBoolean = actual.getBoolean(columnPosition);
            if ("null".equalsIgnoreCase(expected) || "unknown".equalsIgnoreCase(expected)) {
                assertTrue(actual.wasNull());
            } else {
                assertEquals(Boolean.valueOf(expected), actualBoolean);
            }
        }
    },
    DATE {
        @Override
        public void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException, ParseException {
            final Date actualDate = actual.getDate(columnPosition);
            final Supplier<String> messageSupplier = errorMessage(rowPosition, columnPosition);

            if ("null".equalsIgnoreCase(expected)) {
                assertTrue(actual.wasNull(), messageSupplier);
            } else {
                assertEquals(ISO8601.parse(expected), actualDate, messageSupplier);
            }
        }
    },
    DECIMAL {
        @Override
        public void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException {
            final BigDecimal actualDecimal = actual.getBigDecimal(columnPosition);
            final Supplier<String> messageSupplier = errorMessage(rowPosition, columnPosition);

            if ("null".equalsIgnoreCase(expected)) {
                assertTrue(actual.wasNull(), messageSupplier);
            } else {
                assertFalse(actual.wasNull(), messageSupplier);

                // TODO: we might want to be able to tweak decimal comparison with a step: use 1% rule for TPC-H tests and compareTo == 0 for the rest
                final boolean isAcceptable = validateDecimalValue(actualDecimal.doubleValue(), parseDouble(expected));

                assertTrue(isAcceptable, messageSupplier);
            }
        }

        /**
         * Validates that the result is within 1% of the validation data as specified in section 2.1.3.5 of the TPC-H specs
         */
        private boolean validateDecimalValue(double actual, double expected) {
            return 0.99 * expected <= actual && actual <= 1.01 * expected;
        }
    },
    INTEGER {
        @Override
        public void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException {
            final int actualInt = actual.getInt(columnPosition);
            final Supplier<String> messageSupplier = errorMessage(rowPosition, columnPosition);

            if ("null".equalsIgnoreCase(expected)) {
                assertTrue(actual.wasNull(), messageSupplier);
            } else {
                assertEquals(parseInt(expected), actualInt, messageSupplier);
            }
        }
    },
    TEXT {
        @Override
        public void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws SQLException {
            final String actualString = actual.getString(columnPosition);
            final Supplier<String> messageSupplier = errorMessage(rowPosition, columnPosition);

            if (actual.wasNull()) {
                assertTrue("null".equalsIgnoreCase(expected), messageSupplier);
            } else {
                assertEquals(expected, actualString, messageSupplier);
            }
        }
    };

    private static final SimpleDateFormat ISO8601 = iso8601DateFormat();

    private static SimpleDateFormat iso8601DateFormat() {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        format.setTimeZone(TimeZone.getDefault());

        return format;
    }

    public static Checker checkerFor(String typeName) {
        return Checker.valueOf(typeName.toUpperCase());
    }

    private static Supplier<String> errorMessage(int rowIndex, int columnIndex) {
        return () -> "Row " + rowIndex + " column " + columnIndex + ": value mismatch";
    }

    public abstract void check(ResultSet actual, int rowPosition, int columnPosition, String expected) throws Exception;
}
