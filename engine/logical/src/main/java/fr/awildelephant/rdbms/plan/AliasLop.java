package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.alias.Alias;
import fr.awildelephant.rdbms.plan.alias.ColumnAlias;

public final class AliasLop extends AbstractLop {

    private final Alias alias;
    private final LogicalOperator input;

    public AliasLop(LogicalOperator input, Alias alias) {
        super(input.schema().alias(alias::alias));

        this.alias = alias;
        this.input = input;
    }

    // TODO: remove this
    public static AliasLop aliasOperator(ColumnAlias alias, LogicalOperator input) {
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
