package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class JoinOutputSchemaFactory {

    private JoinOutputSchemaFactory() {

    }

    public static Schema innerJoinOutputSchema(Schema leftInputSchema, Schema rightInputSchema) {
        final List<ColumnMetadata> rightInputColumns = rightInputSchema.columnNames().stream()
                .map(rightInputSchema::column)
                .collect(toList());

        return leftInputSchema.extend(rightInputColumns);
    }

    public static Schema leftJoinOutputSchema(Schema leftInputSchema, Schema rightInputSchema) {
        final List<ColumnMetadata> nullableRightInputColumns = rightInputSchema.columnNames().stream()
                .map(rightInputSchema::column)
                .map(JoinOutputSchemaFactory::nullable)
                .collect(toList());

        return leftInputSchema.extend(nullableRightInputColumns);
    }

    private static ColumnMetadata nullable(ColumnMetadata column) {
        return new ColumnMetadata(column.index(), column.name(), column.domain(), false, column.system());
    }
}
