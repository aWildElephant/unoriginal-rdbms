package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.ColumnType;
import fr.awildelephant.rdbms.schema.Domain;

public final class ColumnUtils {

    private ColumnUtils() {

    }

    public static Domain domainOf(ColumnType columnType) {
        return switch (columnType) {
            case BOOLEAN -> Domain.BOOLEAN;
            case BIGINT -> Domain.LONG;
            case DATE -> Domain.DATE;
            case DECIMAL -> Domain.DECIMAL;
            case INTEGER -> Domain.INTEGER;
            case TEXT -> Domain.TEXT;
        };
    }
}
