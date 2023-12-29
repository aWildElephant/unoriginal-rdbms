package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Delete;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.execution.operator.values.RecordValues;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.server.QueryContext;
import fr.awildelephant.rdbms.storage.data.column.VersionColumn;
import fr.awildelephant.rdbms.storage.data.record.Record;
import fr.awildelephant.rdbms.storage.data.table.ManagedTable;
import fr.awildelephant.rdbms.version.PermanentVersion;
import fr.awildelephant.rdbms.version.TemporaryVersion;
import fr.awildelephant.rdbms.version.Version;

import java.util.function.Predicate;

import static fr.awildelephant.rdbms.algebraizer.ASTToValueExpressionTransformer.createValueExpression;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.formula.creation.ValueExpressionToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.schema.Schema.EMPTY_SCHEMA;

public final class DeleteExecutor {

    private final Storage storage;

    public DeleteExecutor(final Storage storage) {
        this.storage = storage;
    }

    public void execute(Delete delete, QueryContext context) {
        context.setUpdate();

        final TemporaryVersion updateVersion = context.temporaryVersion();
        final PermanentVersion readVersion = updateVersion.databaseVersion();

        final ManagedTable table = storage.get(delete.tableName(), readVersion);

        final Predicate<Record> predicate = buildPredicate(delete.predicate(), table.schema(), readVersion);

        final VersionColumn toVersionColumn = table.toVersionColumn();
        int i = 0;
        for (Record record : table) {
            if (predicate.test(record)) {
                toVersionColumn.setGeneric(i, updateVersion);
            }

            i++;
        }
    }

    private Predicate<Record> buildPredicate(final AST ast, final Schema tableSchema, Version version) {
        final Formula formula = createFormula(createValueExpression(ast, tableSchema, EMPTY_SCHEMA), tableSchema);

        final int numberOfAttributes = tableSchema.numberOfAttributes();
        final int fromVersionIndex = numberOfAttributes - 2;
        final int toVersionIndex = numberOfAttributes - 1;

        final RecordValues recordValues = new RecordValues();
        return record -> {
            final Version fromVersion = record.get(fromVersionIndex).getVersion();
            final Version toVersion = record.get(toVersionIndex).getVersion();

            if (fromVersion.isAfter(version) || !toVersion.isAfter(version)) {
                return false;
            }

            recordValues.setRecord(record);

            return trueValue().equals(formula.evaluate(recordValues));
        };
    }
}
