package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.OrderedColumnMetadata;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

public final class JoinOutputSchemaFactory {

    private JoinOutputSchemaFactory() {

    }

    public static Schema innerJoinOutputSchema(Schema leftInputSchema, Schema rightInputSchema) {
        final List<ColumnMetadata> rightInputColumns = rightInputSchema.columnMetadataList();

        return leftInputSchema.extend(rightInputColumns);
    }

    public static Schema leftJoinOutputSchema(Schema leftInputSchema, Schema rightInputSchema) {
        final List<ColumnMetadata> nullableRightInputColumns = rightInputSchema.columnNames().stream()
                .map(rightInputSchema::column)
                .map(OrderedColumnMetadata::metadata)
                .map(JoinOutputSchemaFactory::nullable)
                .toList();

        return leftInputSchema.extend(nullableRightInputColumns);
    }

    public static Schema semiJoinOutputSchema(Schema leftInputSchema, ColumnReference outputColumnName) {
        final ColumnMetadata outputColumn = new ColumnMetadata(outputColumnName, Domain.BOOLEAN, true, true);

        return leftInputSchema.extend(List.of(outputColumn));
    }

    private static ColumnMetadata nullable(ColumnMetadata column) {
        return new ColumnMetadata(column.name(), column.domain(), false, column.system());
    }
}
