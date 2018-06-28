package fr.awildelephant.rdbms.client;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.value.DecimalValue;
import fr.awildelephant.rdbms.engine.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.value.IntegerValue;
import fr.awildelephant.rdbms.engine.data.value.StringValue;
import fr.awildelephant.rdbms.schema.Schema;

import java.math.BigDecimal;
import java.util.Iterator;

public class RDBMSResultSet extends AbstractResultSet {

    private final Schema schema;
    private Iterator<Record> tuples;
    private Record current;
    private boolean isClosed;

    private int cursor = -1;

    RDBMSResultSet(Schema schema, Iterator<Record> tuples) {
        this.schema = schema;
        this.tuples = tuples;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) {
        return ((DecimalValue) field(columnIndex - 1)).value();
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) {
        return ((DecimalValue) field(schema.indexOf(columnLabel))).value();
    }

    @Override
    public int getInt(int columnIndex) {
        return ((IntegerValue) field(columnIndex - 1)).value();
    }

    @Override
    public int getInt(String columnLabel) {
        return ((IntegerValue) field(schema.indexOf(columnLabel))).value();
    }

    @Override
    public String getString(int columnIndex) {
        return ((StringValue) field(columnIndex - 1)).value();
    }

    @Override
    public String getString(String columnLabel) {
        return ((StringValue) field(schema.indexOf(columnLabel))).value();
    }

    @Override
    public int getRow() {
        return cursor + 1;
    }

    @Override
    public boolean next() {
        if (tuples.hasNext()) {
            current = tuples.next();
            // TODO: cursor is not incremented, write an unit test for getRow

            return true;
        }

        return false;
    }

    @Override
    public void close() {
        isClosed = true;
        tuples = null;
        current = null;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    private DomainValue field(int index) {
        return current.get(index);
    }
}
