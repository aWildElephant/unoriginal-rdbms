package fr.awildelephant.rdbms.operator.logical.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;

public interface ReversibleAlias extends Alias {

    ColumnReference unalias(ColumnReference aliased);
}
