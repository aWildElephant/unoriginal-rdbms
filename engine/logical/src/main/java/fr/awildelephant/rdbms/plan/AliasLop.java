package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.alias.Alias;

public final class AliasLop extends AbstractLop {

    private final Alias alias;
    private final LogicalOperator input;

    public AliasLop(LogicalOperator input, Alias alias) {
        super(input.schema().alias(alias::alias));

        this.alias = alias;
        this.input = input;
    }

    public LogicalOperator input() {
        return input;
    }

    public Alias alias() {
        return alias;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
