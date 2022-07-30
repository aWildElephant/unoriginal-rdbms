package fr.awildelephant.rdbms.execution;

import fr.awildelephant.rdbms.execution.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public final class OutputSchemaFactory {

    private OutputSchemaFactory() {

    }

    public static Schema mapOutputSchema(Schema inputSchema, List<ValueExpression> expressions, List<ColumnReference> expressionsOutputNames) {
        final List<ColumnMetadata> expressionsColumns = new ArrayList<>(expressions.size());

        for (int i = 0; i < expressions.size(); i++) {
            expressionsColumns.add(new ColumnMetadata(expressionsOutputNames.get(i), expressions.get(i).domain(),
                    false, false));
        }

        return inputSchema.extend(expressionsColumns);
    }
}
