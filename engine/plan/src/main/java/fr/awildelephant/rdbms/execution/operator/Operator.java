package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;

public interface Operator {

    Table compute(TemporaryStorage storage);
}
