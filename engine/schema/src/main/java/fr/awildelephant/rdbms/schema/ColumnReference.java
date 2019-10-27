package fr.awildelephant.rdbms.schema;

import java.util.Optional;

public interface ColumnReference {

    Optional<String> table();

    String name();

    String fullName();

    ColumnReference renameColumn(String newColumnName);
}
