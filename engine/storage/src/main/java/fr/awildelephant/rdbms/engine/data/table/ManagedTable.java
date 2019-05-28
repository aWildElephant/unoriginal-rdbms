package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.engine.constraint.ConstraintChecker;
import fr.awildelephant.rdbms.engine.data.index.UniqueIndex;

import java.util.List;

public interface ManagedTable extends Table {

    UniqueIndex createIndexOn(List<String> columnNames);

    void addChecker(ConstraintChecker checker);
}
