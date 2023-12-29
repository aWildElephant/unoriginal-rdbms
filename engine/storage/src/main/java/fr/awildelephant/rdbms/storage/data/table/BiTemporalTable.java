package fr.awildelephant.rdbms.storage.data.table;

import fr.awildelephant.rdbms.storage.data.column.VersionColumn;

public interface BiTemporalTable {

    VersionColumn fromVersionColumn();

    VersionColumn toVersionColumn();
}
