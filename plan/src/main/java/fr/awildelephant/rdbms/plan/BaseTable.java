package fr.awildelephant.rdbms.plan;

public class BaseTable implements Plan {

    private final String name;

    public BaseTable(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public <T> T accept(PlanVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
