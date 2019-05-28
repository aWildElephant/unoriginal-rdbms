package fr.awildelephant.rdbms.driver;

import fr.awildelephant.rdbms.jdbc.abstraction.ResultProxy;
import fr.awildelephant.rdbms.jdbc.abstraction.Value;
import fr.awildelephant.rdbms.rpc.generated.Rdbms;

public class RemoteResultProxy implements ResultProxy {

    private final Rdbms.QueryResult result;
    private final ValueProxy valueProxy = new ValueProxy();

    RemoteResultProxy(Rdbms.QueryResult result) {
        this.result = result;
    }

    @Override
    public Value get(int row, int column) {
        valueProxy.setValue(result.getRows(row).getValue(column));

        return valueProxy;
    }

    @Override
    public int position(String columnName) {
        for (int i = 0; i < result.getSchemaCount(); i++) {
            if (result.getSchema(i).getName().equals(columnName)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public String columnName(int column) {
        return result.getSchema(column).getName();
    }

    @Override
    public String columnTypeName(int column) {
        return result.getSchema(column).getType().name();
    }

    @Override
    public boolean isNullable(int column) {
        return result.getSchema(column).getNullable();
    }

    @Override
    public int numberOfRows() {
        return result.getRowsCount();
    }

    @Override
    public int numberOfColumns() {
        return result.getSchemaCount();
    }
}
