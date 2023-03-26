package fr.awildelephant.rdbms.jdbc;

import fr.awildelephant.rdbms.embedded.TableProxy;
import fr.awildelephant.rdbms.engine.data.table.TableFactory;
import fr.awildelephant.rdbms.engine.data.table.WriteableTable;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static org.assertj.core.api.Assertions.assertThat;

class RDBMSResultSetTest {

    @Test
    void getInt_should_return_0_for_a_null() throws SQLException {
        final ColumnMetadata column = new ColumnMetadata(new UnqualifiedColumnReference("result"), Domain.INTEGER, false, false);
        final WriteableTable table = TableFactory.simpleTable(Schema.of(column), 1);
        table.columns().get(0).add(nullValue());
        try (RDBMSResultSet resultSet = new RDBMSResultSet(new TableProxy(table))) {
            resultSet.next();
            assertThat(resultSet.getInt(1)).isZero();
        }
    }
}