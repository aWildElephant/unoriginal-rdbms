package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.plan.Plan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Engine {

    private final Map<String, Table> tables = new HashMap<>();

    public void createTable(final String name, final List<String> columnNames) {
        throw new UnsupportedOperationException();
    }

    public void insert(String tableName, List<List<Integer>> values) {
        throw new UnsupportedOperationException();
    }

    public Table execute(Plan logicalPlan) {
        throw new UnsupportedOperationException();
    }
}
