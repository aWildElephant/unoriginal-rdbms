package fr.awildelephant.rdbms.schema;

public record ColumnMetadata(ColumnReference name, Domain domain, boolean notNull, boolean system) {
}
