package fr.awildelephant.rdbms.client;

import fr.awildelephant.rdbms.engine.data.Table;
import fr.awildelephant.rdbms.engine.data.value.IntegerValue;
import fr.awildelephant.rdbms.engine.data.value.StringValue;

public class RDBMSResultSet extends AbstractResultSet {

    private final Table result;
    private boolean isClosed;

    private int cursor = -1;

    RDBMSResultSet(final Table result) {
        this.result = result;
    }

    @Override
    public int getInt(int columnIndex) {
        return ((IntegerValue) result.get(cursor).get(columnIndex - 1)).value();
    }

    @Override
    public int getInt(String columnLabel) {
        return ((IntegerValue) result.get(cursor).get(columnLabel)).value();
    }

    @Override
    public String getString(int columnIndex) {
        return ((StringValue) result.get(cursor).get(columnIndex - 1)).value();
    }

    @Override
    public String getString(String columnLabel) {
        return ((StringValue) result.get(cursor).get(columnLabel)).value();
    }

    @Override
    public int getRow() {
        return cursor + 1;
    }

    @Override
    public boolean next() {
        if (cursor < result.size() - 1) {
            cursor++;

            return true;
        }

        return false;
    }

    @Override
    public void close() {
        isClosed = true;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }
}
