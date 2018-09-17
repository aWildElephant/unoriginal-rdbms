package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.table.Table;

public interface Operator {

    Table compute(Table inputTable);
}
