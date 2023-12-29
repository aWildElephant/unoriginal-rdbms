package fr.awildelephant.rdbms.storage.data.table;

import fr.awildelephant.rdbms.storage.data.column.WriteableColumn;

public interface BiTemporalTable {

    WriteableColumn fromVersionColumn();

    WriteableColumn toVersionColumn();
}
