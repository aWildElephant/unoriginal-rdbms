package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.table.Table;

public interface JoinOperator {

    Table compute(Table left, Table right);
}
