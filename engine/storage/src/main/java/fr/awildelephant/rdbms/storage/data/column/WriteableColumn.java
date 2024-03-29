package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface WriteableColumn extends AppendableColumn {

    void set(int index, DomainValue value);
}
