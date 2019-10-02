package fr.awildelephant.rdbms.schema;

import java.util.Optional;

public interface ColumnReference {

    Optional<String> qualifier();

    String name();
}
