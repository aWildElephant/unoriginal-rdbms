package fr.awildelephant.rdbms.plan;

import java.util.Set;

public class Projection implements Plan {

    private final Set<String> columnNames;
    private final Plan input;

    public Projection(Set<String> columnNames, Plan input) {
        this.columnNames = columnNames;
        this.input = input;
    }
}
