package fr.awildelephant.rdbms.client;

import fr.awildelephant.rdbms.engine.data.tuple.Tuple;
import fr.awildelephant.rdbms.engine.data.value.IntegerValue;
import fr.awildelephant.rdbms.engine.data.value.StringValue;

import java.util.Iterator;

public class RDBMSResultSet extends AbstractResultSet {

    private Iterator<Tuple> tuples;
    private Tuple current;
    private boolean isClosed;

    private int cursor = -1;

    RDBMSResultSet(Iterator<Tuple> tuples) {
        this.tuples = tuples;
    }

    @Override
    public int getInt(int columnIndex) {
        return ((IntegerValue) current.get(columnIndex - 1)).value();
    }

    @Override
    public int getInt(String columnLabel) {
        return ((IntegerValue) current.get(columnLabel)).value();
    }

    @Override
    public String getString(int columnIndex) {
        return ((StringValue) current.get(columnIndex - 1)).value();
    }

    @Override
    public String getString(String columnLabel) {
        return ((StringValue) current.get(columnLabel)).value();
    }

    @Override
    public int getRow() {
        return cursor + 1;
    }

    @Override
    public boolean next() {
        if (tuples.hasNext()) {
            current = tuples.next();

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
}
