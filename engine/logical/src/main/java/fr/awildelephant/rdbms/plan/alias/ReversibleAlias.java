package fr.awildelephant.rdbms.plan.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;

public interface ReversibleAlias extends Alias {

    ColumnReference unalias(ColumnReference aliased);
}
