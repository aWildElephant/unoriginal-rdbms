package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.data.Table;
import fr.awildelephant.rdbms.engine.data.Tuple;
import fr.awildelephant.rdbms.engine.data.domain.DomainValue;
import fr.awildelephant.rdbms.plan.Plan;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.*;

public final class Engine {

    private final Map<String, Table> tables = new HashMap<>();

    public void createTable(final String tableName, final List<String> attributes) {
        final Map<String, Integer> attributeMapping = new HashMap<>();

        for (int i = 0; i < attributes.size(); i++) {
            attributeMapping.put(attributes.get(i), i);
        }

        tables.put(tableName, new Table(new Schema(attributeMapping)));
    }

    public void insert(final String tableName, final Collection<DomainValue[]> rows) {
        final Table table = tables.get(tableName);
        final Schema schema = table.schema();

        for (DomainValue[] row : rows) {
            table.add(new Tuple(schema, row));
        }
    }

    public Table execute(final Plan logicalPlan) {
        return logicalPlan.accept(new PlanExecutor(tables));
    }

    public Optional<Schema> schemaOf(String tableName) {
        final Table table = tables.get(tableName);

        if (table == null) {
            return Optional.empty();
        }

        return Optional.of(table.schema());
    }
}
