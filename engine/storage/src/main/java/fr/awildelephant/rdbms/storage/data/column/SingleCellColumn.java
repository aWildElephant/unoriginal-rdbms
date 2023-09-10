package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;

public class SingleCellColumn implements Column {

    private final DomainValue value;

    public SingleCellColumn(DomainValue value) {
        this.value = value;
    }

    @Override
    public DomainValue get(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }

        return value;
    }

    @Override
    public int size() {
        return 1;
    }
}
