package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.data.value.IntegerValue;
import fr.awildelephant.rdbms.engine.data.column.Column;
import fr.awildelephant.rdbms.engine.data.column.SingleCellColumn;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.record.SingleCellRecord;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

import static fr.awildelephant.rdbms.data.value.IntegerValue.integerValue;

public class UpdateResultTable implements Table {

    private static final Schema SCHEMA = Schema.of(new ColumnMetadata(new UnqualifiedColumnReference("result"), Domain.INTEGER, true, true));

    private final List<? extends Column> columns;
    private final List<Record> records;

    public UpdateResultTable(int value) {
        final IntegerValue domainValue = integerValue(value);

        columns = List.of(new SingleCellColumn(domainValue));
        records = List.of(new SingleCellRecord(domainValue));
    }

    @Override
    public Schema schema() {
        return SCHEMA;
    }

    @Override
    public int numberOfTuples() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public List<? extends Column> columns() {
        return columns;
    }

    @NotNull
    @Override
    public Iterator<Record> iterator() {
        return records.iterator();
    }
}
