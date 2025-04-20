package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.jdbc.abstraction.ResultProxy;
import fr.awildelephant.rdbms.jdbc.abstraction.Value;
import fr.awildelephant.rdbms.jdbc.exception.ResourceClosedSQLException;
import fr.awildelephant.rdbms.util.structure.matrix.Matrix;
import fr.awildelephant.rdbms.util.structure.matrix.MatrixFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RDBMSResultSetTest {

    private final Matrix<Value> TWO_ROWS = MatrixFactory.fromRowBasedData(2, 1, new MockStringValue("first_row"), new MockStringValue("second_row"));

    @Test
    void getInt_should_return_0_for_a_null() throws SQLException {
        try (final RDBMSResultSet resultSet = new RDBMSResultSet(new SingleNullCellResultProxy())) {
            resultSet.next();
            assertThat(resultSet.getInt(1)).isZero();
        }
    }

    @Test
    void getInt_should_throw_if_the_result_set_is_closed() {
        final RDBMSResultSet resultSet = new RDBMSResultSet(new MockResultProxy(TWO_ROWS));
        resultSet.next();
        resultSet.close();
        assertThrows(ResourceClosedSQLException.class, () -> resultSet.getInt(1));
    }

    @Test
    void isBeforeFirst_should_return_true_before_the_first_call_to_next() throws SQLException {
        try (final RDBMSResultSet resultSet = new RDBMSResultSet(new MockResultProxy(TWO_ROWS))) {
            assertThat(resultSet.isBeforeFirst()).isTrue();
        }
    }

    @Test
    void isBeforeFirst_should_return_false_after_the_first_call_to_next() throws SQLException {
        try (final RDBMSResultSet resultSet = new RDBMSResultSet(new MockResultProxy(TWO_ROWS))) {
            resultSet.next();
            assertThat(resultSet.isBeforeFirst()).isFalse();
        }
    }

    @Test
    void isBeforeFirst_should_throw_if_the_result_set_is_closed() {
        final RDBMSResultSet resultSet = new RDBMSResultSet(new MockResultProxy(TWO_ROWS));
        resultSet.close();
        assertThrows(ResourceClosedSQLException.class, resultSet::isBeforeFirst);
    }

    @Test
    void isFirst_should_return_false_before_the_first_call_to_next() throws SQLException {
        try (final RDBMSResultSet resultSet = new RDBMSResultSet(new MockResultProxy(TWO_ROWS))) {
            assertThat(resultSet.isFirst()).isFalse();
        }
    }

    @Test
    void isFirst_should_return_true_after_the_first_call_to_next() throws SQLException {
        try (final RDBMSResultSet resultSet = new RDBMSResultSet(new MockResultProxy(TWO_ROWS))) {
            resultSet.next();
            assertThat(resultSet.isFirst()).isTrue();
        }
    }

    @Test
    void isFirst_should_return_false_after_the_second_call_to_next() throws SQLException {
        try (final RDBMSResultSet resultSet = new RDBMSResultSet(new MockResultProxy(TWO_ROWS))) {
            resultSet.next();
            resultSet.next();
            assertThat(resultSet.isFirst()).isFalse();
        }
    }

    @Test
    void isFirst_should_throw_if_the_result_set_is_closed() {
        final RDBMSResultSet resultSet = new RDBMSResultSet(new MockResultProxy(TWO_ROWS));
        resultSet.close();
        assertThrows(ResourceClosedSQLException.class, resultSet::isFirst);
    }

    @Test
    void beforeFirst_should_position_the_cursor_before_the_first_row() throws SQLException {
        try (final RDBMSResultSet resultSet = new RDBMSResultSet(new MockResultProxy(TWO_ROWS))) {
            resultSet.next();
            resultSet.next();
            resultSet.beforeFirst();
            assertThat(resultSet.isBeforeFirst()).isTrue();
        }
    }

    @Test
    void first_should_position_the_cursor_on_the_first_row() throws SQLException {
        try (final RDBMSResultSet resultSet = new RDBMSResultSet(new MockResultProxy(TWO_ROWS))) {
            resultSet.next();
            resultSet.next();
            resultSet.first();
            assertThat(resultSet.isFirst()).isTrue();
        }
    }

    private static final class SingleNullCellResultProxy implements ResultProxy {

        @Override
        public Value get(int row, int column) {
            return new Value() {
                @Override
                public boolean isNull() {
                    return true;
                }

                @Override
                public boolean getBoolean() {
                    return false;
                }

                @Override
                public BigDecimal getBigDecimal() {
                    return null;
                }

                @Override
                public Date getDate() {
                    return null;
                }

                @Override
                public int getInt() {
                    return 0;
                }

                @Override
                public long getLong() {
                    return 0;
                }

                @Override
                public String getString() {
                    return null;
                }
            };
        }

        @Override
        public int position(String columnName) {
            return 0;
        }

        @Override
        public String columnName(int column) {
            return "test";
        }

        @Override
        public String columnTypeName(int column) {
            return "INTEGER";
        }

        @Override
        public boolean isNullable(int column) {
            return false;
        }

        @Override
        public int numberOfRows() {
            return 1;
        }

        @Override
        public int numberOfColumns() {
            return 1;
        }
    }
}