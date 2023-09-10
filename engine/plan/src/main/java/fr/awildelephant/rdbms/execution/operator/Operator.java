package fr.awildelephant.rdbms.execution.operator;

import fr.awildelephant.rdbms.execution.executor.TemporaryStorage;
import fr.awildelephant.rdbms.storage.data.table.Table;

public interface Operator {

    Table compute(TemporaryStorage storage);
}
