package fr.awildelephant.rdbms.engine.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface AppendOnlyColumn extends Column {

    void add(DomainValue value);

    void ensureCapacity(int capacity);
}
