package fr.awildelephant.rdbms.storage.data.table;

import fr.awildelephant.rdbms.storage.constraint.ConstraintChecker;
import fr.awildelephant.rdbms.storage.data.index.UniqueIndex;

import java.util.List;

public interface ManagedTable extends WriteableTable, BiTemporalTable {

    UniqueIndex createIndexOn(List<String> columnNames);

    void addChecker(ConstraintChecker checker);
}
