package fr.awildelephant.rdbms.execution.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;

public interface Alias {

    ColumnReference alias(ColumnReference original);
}
