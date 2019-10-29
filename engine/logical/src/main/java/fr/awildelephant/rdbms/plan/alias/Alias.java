package fr.awildelephant.rdbms.plan.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;

public interface Alias {

    ColumnReference alias(ColumnReference original);

    ColumnReference unalias(ColumnReference aliased);
}
