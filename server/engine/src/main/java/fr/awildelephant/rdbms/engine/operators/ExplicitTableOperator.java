package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.engine.data.record.Record;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static fr.awildelephant.rdbms.engine.data.table.TableFactory.simpleTable;
import static java.util.Collections.emptyMap;

public class ExplicitTableOperator implements Operator<Void, Table> {

    private final List<List<Formula>> matrix;
    private final Schema schema;

    public ExplicitTableOperator(List<List<Formula>> matrix, Schema schema) {
        this.matrix = matrix;
        this.schema = schema;
    }

    @Override
    public Table compute(Void unused) {
        final Table table = simpleTable(schema, matrix.size());

        for (List<Formula> row : matrix) {
            final DomainValue[] tuple = new DomainValue[row.size()];

            for (int i = 0; i < row.size(); i++) {
                tuple[i] = row.get(i).evaluate(emptyMap());
            }

            table.add(new Record(tuple));
        }

        return table;
    }
}
