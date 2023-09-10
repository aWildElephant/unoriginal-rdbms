package fr.awildelephant.rdbms.storage.data.record;

import fr.awildelephant.rdbms.data.value.DomainValue;

public class SingleCellRecord implements Record {

    private final DomainValue value;

    public SingleCellRecord(DomainValue value) {
        this.value = value;
    }

    @Override
    public DomainValue get(int attributeIndex) {
        if (attributeIndex != 0) {
            throw new IndexOutOfBoundsException();
        }

        return value;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Record materialize() {
        return this;
    }
}
