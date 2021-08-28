package fr.awildelephant.rdbms.rpc.server;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.data.value.FalseValue;
import fr.awildelephant.rdbms.data.value.IntegerValue;
import fr.awildelephant.rdbms.data.value.TextValue;
import fr.awildelephant.rdbms.data.value.TrueValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.rpc.generated.RDBMSGrpc;
import fr.awildelephant.rdbms.rpc.generated.Rdbms;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.server.RDBMS;
import io.grpc.stub.StreamObserver;

public class RDBMSServer extends RDBMSGrpc.RDBMSImplBase {

    private static final Rdbms.Value FALSE_VALUE_MESSAGE = Rdbms.Value.newBuilder().setBooleanValue(
            Rdbms.BooleanValue.newBuilder().setValue(false).build()).build();
    private static final Rdbms.Value TRUE_VALUE_MESSAGE = Rdbms.Value.newBuilder().setBooleanValue(
            Rdbms.BooleanValue.newBuilder().setValue(true).build()).build();
    private static final Rdbms.Value NULL_VALUE_MESSAGE = Rdbms.Value.newBuilder()
                                                                     .setNullValue(Rdbms.NullValue.newBuilder().build())
                                                                     .build();
    private static final Rdbms.UpdateResult DUMMY_UPDATE_RESULT = Rdbms.UpdateResult.newBuilder()
                                                                                    .setNumberOfUpdatedRows(0).build();

    private final RDBMS rdbms;

    RDBMSServer(RDBMS rdbms) {
        this.rdbms = rdbms;
    }

    @Override
    public void execute(Rdbms.Query request, StreamObserver<Rdbms.Result> observer) {
        final Table resultTable = rdbms.execute(request.getQuery());

        final Rdbms.Result.Builder resultBuilder = Rdbms.Result.newBuilder();

        if (resultTable != null) {
            resultBuilder.setQuery(queryResult(resultTable));
        } else {
            resultBuilder.setUpdate(DUMMY_UPDATE_RESULT);
        }

        observer.onNext(resultBuilder.build());

        observer.onCompleted();
    }

    private Rdbms.QueryResult.Builder queryResult(Table table) {
        final Rdbms.QueryResult.Builder queryResultBuilder = Rdbms.QueryResult.newBuilder();

        final Schema schema = table.schema();

        for (ColumnReference columnReference : schema.columnNames()) {
            final Rdbms.QueryResult.Column.Builder columnBuilder = Rdbms.QueryResult.Column.newBuilder();

            final String fullName = columnReference.table()
                                                   .map(tableName -> tableName + '.' + columnReference.name())
                                                   .orElseGet(columnReference::name);

            columnBuilder.setName(fullName);
            columnBuilder.setType(getType(schema, columnReference));

            queryResultBuilder.addSchema(columnBuilder);
        }

        for (Record record : table) {
            queryResultBuilder.addRows(toRow(record));
        }

        return queryResultBuilder;
    }

    private Rdbms.QueryResult.Type getType(Schema schema, ColumnReference columnReference) {
        return switch (schema.column(columnReference).domain()) {
            case BOOLEAN -> Rdbms.QueryResult.Type.BOOLEAN;
            case DECIMAL -> Rdbms.QueryResult.Type.DECIMAL;
            case INTEGER -> Rdbms.QueryResult.Type.INTEGER;
            case TEXT -> Rdbms.QueryResult.Type.TEXT;
            default -> throw new IllegalStateException();
        };
    }

    private Rdbms.QueryResult.Row.Builder toRow(Record record) {
        final Rdbms.QueryResult.Row.Builder rowBuilder = Rdbms.QueryResult.Row.newBuilder();

        for (int i = 0; i < record.size(); i++) {
            rowBuilder.addValue(toValue(record.get(i)));
        }

        return rowBuilder;
    }

    private Rdbms.Value toValue(DomainValue value) {
        if (value.isNull()) {
            return NULL_VALUE_MESSAGE;
        }

        if (value instanceof TrueValue) {
            return TRUE_VALUE_MESSAGE;
        } else if (value instanceof FalseValue) {
            return FALSE_VALUE_MESSAGE;
        }

        final Rdbms.Value.Builder valueBuilder = Rdbms.Value.newBuilder();
        if (value instanceof IntegerValue) {
            valueBuilder.setIntegerValue(Rdbms.IntegerValue.newBuilder().setValue(value.getInt()));
        } else if (value instanceof TextValue) {
            valueBuilder.setTextValue(Rdbms.TextValue.newBuilder().setValue(value.getString()));
        } else {
            throw new UnsupportedOperationException();
        }

        return valueBuilder.build();
    }
}
