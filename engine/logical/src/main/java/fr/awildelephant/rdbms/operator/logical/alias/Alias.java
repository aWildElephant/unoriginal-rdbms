package fr.awildelephant.rdbms.operator.logical.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;

public interface Alias {

    ColumnReference alias(ColumnReference original);
}
