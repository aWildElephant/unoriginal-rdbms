package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface Column {

    DomainValue get(int index);

    int size();
}
