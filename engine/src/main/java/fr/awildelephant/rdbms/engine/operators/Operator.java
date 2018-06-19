package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.Table;

public interface Operator {

    Table compute(Table inputTable);
}
