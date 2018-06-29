package fr.awildelephant.rdbms.client;

import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Schema;

import java.math.BigDecimal;
import java.util.Iterator;

public class RDBMSResultSet extends AbstractResultSet {

    private final Schema schema;
    private Iterator<Record> tuples;
    private Record current;
    private boolean isClosed;
    private boolean wasNull;

    private int cursor = -1;

    RDBMSResultSet(Schema schema, Iterator<Record> tuples) {
        this.schema = schema;
        this.tuples = tuples;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) {
        final DomainValue value = field(columnIndex - 1);

        if (value.isNull()) {
            wasNull = true;

            return null;
        }

        return value.getBigDecimal();
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) {
        return getBigDecimal(schema.indexOf(columnLabel) + 1);
    }

    @Override // TODO: write an unit test to see that we return 0 if the value is NULL
    public int getInt(int columnIndex) {
        final DomainValue value = field(columnIndex - 1);

        if (value.isNull()) {
            wasNull = true;
            return 0;
        }

        return value.getInt();
    }

    @Override
    public int getInt(String columnLabel) {
        return getInt(schema.indexOf(columnLabel) + 1);
    }

    @Override
    public String getString(int columnIndex) {
        final DomainValue value = field(columnIndex - 1);

        if (value.isNull()) {
            wasNull = true;

            return null;
        }

        return value.getString();
    }

    @Override
    public String getString(String columnLabel) {
        return getString(schema.indexOf(columnLabel) + 1);
    }

    @Override
    public int getRow() {
        return cursor + 1;
    }

    @Override
    public boolean wasNull() {
        return wasNull;
    }

    @Override
    public boolean next() {
        if (tuples.hasNext()) {
            wasNull = false;
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
