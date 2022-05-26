package fr.awildelephant.rdbms.explain;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.record.Tuple;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.data.table.WriteableTable;
import fr.awildelephant.rdbms.schema.ColumnMetadata;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.List;

import static fr.awildelephant.rdbms.data.value.TextValue.textValue;
import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;

public final class ExplanationTableBuilder {

    public Table build(List<String> explanation) {
        final WriteableTable table = explainTable();
        explanation.stream()
                .map(this::record)
                .forEach(table::add);
        return table;
    }

    private WriteableTable explainTable() {
        final ColumnMetadata column = new ColumnMetadata(new UnqualifiedColumnReference("explain"), Domain.TEXT, true, false);
        return simpleTable(Schema.of(column));
    }

    private Record record(String value) {
        return new Tuple(new DomainValue[]{textValue(value)});
    }
}
