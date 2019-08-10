package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Alias;

public final class AliasLop extends AbstractLop {

    private final Alias alias;
    private final LogicalOperator input;

    private AliasLop(LogicalOperator input, Alias alias) {
        super(input.schema().alias(alias));

        this.alias = alias;
        this.input = input;
    }

    public static AliasLop aliasOperator(Alias alias, LogicalOperator input) {
        return new AliasLop(input, alias);
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
