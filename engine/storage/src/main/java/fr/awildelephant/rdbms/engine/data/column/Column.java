package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface Column {

    DomainValue get(int index);

    // TODO: move add and ensureCapacity to another interface
    void add(DomainValue value);

    void ensureCapacity(int capacity);

    int size();
}
