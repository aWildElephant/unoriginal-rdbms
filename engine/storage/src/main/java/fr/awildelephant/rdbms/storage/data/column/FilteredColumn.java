package fr.awildelephant.rdbms.storage.data.column;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.storage.bitmap.Bitmap;

public class FilteredColumn implements Column {

    private final Column wrappedColumn;
    private final Bitmap bitmap;

    public FilteredColumn(Column wrappedColumn, Bitmap bitmap) {
        this.wrappedColumn = wrappedColumn;
        this.bitmap = bitmap;
    }

    @Override
    public DomainValue get(int index) {
        return wrappedColumn.get(bitmap.getBySetBitIndex(index));
    }

    @Override
    public int size() {
        return bitmap.cardinality();
    }
}
