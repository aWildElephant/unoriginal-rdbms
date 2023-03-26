package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.jdbc.abstraction.ResultProxy;
import fr.awildelephant.rdbms.jdbc.abstraction.Value;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class RDBMSResultSetTest {

    @Test
    void getInt_should_return_0_for_a_null() throws SQLException {
        try (RDBMSResultSet resultSet = new RDBMSResultSet(new SingleNullCellResultProxy())) {
            resultSet.next();
            assertThat(resultSet.getInt(1)).isZero();
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