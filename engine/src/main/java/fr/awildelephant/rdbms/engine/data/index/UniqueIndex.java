package fr.awildelephant.rdbms.engine.data.index;

import fr.awildelephant.rdbms.engine.data.value.DomainValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueIndex {

    private final Set<List<DomainValue>> index = new HashSet<>();

    public void add(List<DomainValue> values) {
        index.add(values);
    }

    public boolean contains(List<DomainValue> values) {
        return index.contains(values);
    }
}
