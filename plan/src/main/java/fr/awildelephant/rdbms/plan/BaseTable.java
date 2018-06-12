package fr.awildelephant.rdbms.plan;

public class BaseTable implements Plan {

    private final String tableName;

    public BaseTable(String tableName) {
        this.tableName = tableName;
    }
}
