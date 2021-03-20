package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public final class OutputSchemaFactory {

    private OutputSchemaFactory() {

    }

    public static Schema mapOutputSchema(Schema inputSchema, List<ValueExpression> expressions, List<ColumnReference> expressionsOutputNames) {
        // TODO: can we use Schema#extend
        final List<ColumnReference> inputColumns = inputSchema.columnNames();

        final List<ColumnMetadata> outputColumns = new ArrayList<>(inputColumns.size() + expressions.size());

        for (ColumnReference columnReference : inputColumns) {
            outputColumns.add(inputSchema.column(columnReference));
        }

        int index = inputColumns.size();

        for (int i = 0; i < expressions.size(); i++) {
            outputColumns.add(new ColumnMetadata(index++, expressionsOutputNames.get(i), expressions.get(i).domain(),
                                                 false, false));
        }

        return new Schema(outputColumns);
    }
}
