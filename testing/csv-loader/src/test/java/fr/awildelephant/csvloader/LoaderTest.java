package fr.awildelephant.csvloader;

import fr.awildelephant.rdbms.client.RDBMSDriver;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class LoaderTest {

    @Test
    @Disabled
    void it_should_load_compressed_lineitem_csv_file() throws SQLException, IOException {
        try (final Connection connection = new RDBMSDriver().connect("jdbc:rdbms:mem:tpch", null)) {
            final Statement createLineitem = connection.createStatement();
            createLineitem.execute("CREATE TABLE lineitem (\n" +
                                           "    l_orderkey INTEGER NOT NULL,\n" +
                                           "    l_partkey INTEGER NOT NULL,\n" +
                                           "    l_suppkey INTEGER NOT NULL,\n" +
                                           "    l_linenumber INTEGER NOT NULL,\n" +
                                           "    l_quantity DECIMAL NOT NULL,\n" +
                                           "    l_extendedprice DECIMAL NOT NULL,\n" +
                                           "    l_discount DECIMAL NOT NULL,\n" +
                                           "    l_tax DECIMAL NOT NULL,\n" +
                                           "    l_returnflag CHAR(1) NOT NULL,\n" +
                                           "    l_linestatus CHAR(1) NOT NULL,\n" +
                                           "    l_shipdate DATE NOT NULL,\n" +
                                           "    l_commitdate DATE NOT NULL,\n" +
                                           "    l_receiptdate DATE NOT NULL,\n" +
                                           "    l_shipinstruct CHAR(25) NOT NULL,\n" +
                                           "    l_shipmode CHAR(10) NOT NULL,\n" +
                                           "    l_comment VARCHAR(44) NOT NULL,\n" +
                                           "    UNIQUE(l_orderkey, l_linenumber)\n" +
                                           ");");
            createLineitem.close();

            new Loader(connection).load(new File("/home/etienne/Downloads/tpch/lineitem.tbl.gz"), "lineitem");

            final Statement statement = connection.createStatement();
            statement.execute("SELECT count(*) FROM lineitem");
            final ResultSet resultSet = statement.getResultSet();
            resultSet.next();

            System.out.println(resultSet.getInt(1));
        }
    }
}