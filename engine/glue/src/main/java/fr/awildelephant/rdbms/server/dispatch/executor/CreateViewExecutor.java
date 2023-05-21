package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.algebraizer.AlgebraizerFactory;
import fr.awildelephant.rdbms.ast.CreateView;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.execution.AliasLop;
import fr.awildelephant.rdbms.execution.LogicalOperator;
import fr.awildelephant.rdbms.execution.alias.ColumnAliasBuilder;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.server.QueryContext;

import java.util.List;

public final class CreateViewExecutor {

    private final AlgebraizerFactory algebraizerFactory;
    private final Storage storage;

    public CreateViewExecutor(AlgebraizerFactory algebraizerFactory, Storage storage) {
        this.algebraizerFactory = algebraizerFactory;
        this.storage = storage;
    }

    public void execute(CreateView createView, QueryContext context) {
        context.setUpdate();

        final LogicalOperator query = algebraizerFactory.build(context.temporaryVersion().databaseVersion()).apply(createView.query());

        final List<String> columnNames = createView.columnNames();
        final Schema querySchema = query.schema();
        if (querySchema.numberOfAttributes() != columnNames.size()) {
            throw new IllegalStateException();
        }

        final ColumnAliasBuilder columnAliasBuilder = new ColumnAliasBuilder();

        final List<ColumnReference> queryOutputColumns = querySchema.columnNames();
        for (int i = 0; i < queryOutputColumns.size(); i++) {
            columnAliasBuilder.add(queryOutputColumns.get(i), columnNames.get(i));
        }

        final AliasLop viewOperator = new AliasLop(query, columnAliasBuilder.build().orElseThrow());

        storage.createView(createView.name(), viewOperator, context.temporaryVersion());
    }
}
