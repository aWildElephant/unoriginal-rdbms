package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface Column {

    DomainValue get(int index);

    void add(DomainValue value);

    void ensureCapacity(int capacity);

    int size();
}
