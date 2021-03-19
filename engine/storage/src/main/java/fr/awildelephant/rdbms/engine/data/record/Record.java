package fr.awildelephant.rdbms.engine.data.record;

import fr.awildelephant.rdbms.data.value.DomainValue;

public interface Record {

    DomainValue get(int attributeIndex);

    int size();

    Record materialize();
}
